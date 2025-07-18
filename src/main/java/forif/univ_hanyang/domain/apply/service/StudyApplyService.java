package forif.univ_hanyang.domain.apply.service;

import forif.univ_hanyang.domain.apply.dto.request.MoveToStudyRequest;
import forif.univ_hanyang.domain.apply.dto.request.StudyApplyRequest;
import forif.univ_hanyang.domain.apply.dto.response.StudyApplyResponse;
import forif.univ_hanyang.domain.apply.entity.StudyApply;
import forif.univ_hanyang.domain.apply.entity.StudyApplyPlan;
import forif.univ_hanyang.domain.apply.repository.StudyApplyRepository;
import forif.univ_hanyang.domain.study.entity.MentorStudy;
import forif.univ_hanyang.domain.study.entity.Study;
import forif.univ_hanyang.domain.study.entity.StudyPlan;
import forif.univ_hanyang.domain.study.repository.MentorStudyRepository;
import forif.univ_hanyang.domain.study.repository.StudyRepository;
import forif.univ_hanyang.domain.user.entity.User;
import forif.univ_hanyang.domain.user.repository.UserRepository;
import forif.univ_hanyang.common.exception.ErrorCode;
import forif.univ_hanyang.common.exception.ForifException;
import forif.univ_hanyang.common.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StudyApplyService {
    private final StudyApplyRepository studyApplyRepository;
    private final StudyRepository studyRepository;
    private final MentorStudyRepository mentorStudyRepository;
    private final UserRepository userRepository;

    @Transactional
    public void applyStudy(StudyApplyRequest request) {
        StudyApply newStudy = new StudyApply();
        setStudyApply(request, newStudy);
    }

    @Transactional(readOnly = true)
    public List<StudyApplyResponse> getAppliedStudies(User admin, Integer year, Integer semester) {
        if (admin.getAuthLv() < 3) {
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        List<StudyApply> studyApplies = studyApplyRepository.findAllByActYearAndActSemester(year, semester);
        if (studyApplies.isEmpty()) {
            return Collections.emptyList();
        }

        return studyApplies.stream()
                .map(studyApply -> {
                    if (studyApply.getPrimaryMentor() == null) {
                        throw new ForifException(ErrorCode.STUDY_APPLICATION_PERIOD_ENDED);
                    }

                    StudyApplyResponse response = StudyApplyResponse.from(studyApply);

                    Optional.ofNullable(studyApply.getSecondaryMentor())
                            .ifPresent(secondaryMentor -> {
                                response.setSecondaryMentorId(secondaryMentor.getId());
                                response.setSecondaryMentorName(secondaryMentor.getName());
                                response.setSecondaryMentorEmail(secondaryMentor.getEmail());
                                response.setSecondaryMentorPhoneNumber(secondaryMentor.getPhoneNumber());
                            });

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStudy(StudyApplyRequest request, User admin, Integer applyId) {
        if(admin.getAuthLv() < 3)
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);

        StudyApply studyApply = studyApplyRepository.findById(applyId)
                .orElseThrow(() -> new ForifException(ErrorCode.STUDY_APPLY_NOT_FOUND));
        setStudyApply(request, studyApply);
    }

    @Transactional
    public void moveToStudy(User admin, MoveToStudyRequest request) {
        if (admin.getAuthLv() < 3)
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);

        List<StudyApply> studyApplies = studyApplyRepository.findAllWithMentorsById(request.getIdList());

        Set<Long> mentorIds = studyApplies.stream()
                .flatMap(apply -> {
                    Stream<Long> ids = Stream.of(apply.getPrimaryMentor().getId());
                    return apply.getSecondaryMentor() != null ?
                            Stream.concat(ids, Stream.of(apply.getSecondaryMentor().getId())) :
                            ids;

                })
                .collect(Collectors.toSet());

        Map<Long, User> mentorMap = userRepository.findAllById(mentorIds).stream()
                .collect(Collectors.toMap(User::getId, mentor -> mentor));

        for (StudyApply studyApply : studyApplies) {
            if (studyApply.getAcceptanceStatus() == 1) {
                throw new ForifException(ErrorCode.STUDY_ALREADY_APPROVED);
            }

            studyApply.setAcceptanceStatus(1);

            User primaryMentor = mentorMap.get(studyApply.getPrimaryMentor().getId());
            if (primaryMentor == null) {
                throw new ForifException(ErrorCode.FIRST_MENTOR_NOT_FOUND);
            }

            User secondaryMentor = studyApply.getSecondaryMentor() != null ?
                    mentorMap.get(studyApply.getSecondaryMentor().getId()) : null;
            if (studyApply.getSecondaryMentor() != null && secondaryMentor == null) {
                throw new ForifException(ErrorCode.SECOND_MENTOR_NOT_FOUND);
            }

            Study study = createStudyFromApply(studyApply, primaryMentor, secondaryMentor);

            List<StudyPlan> studyPlans = convertToStudyPlans(studyApply.getStudyApplyPlans(), study);
            study.setStudyPlans(studyPlans);

            Study savedStudy = studyRepository.save(study);

            if (secondaryMentor != null) {
                setMentor(savedStudy, primaryMentor.getId(), 2);
                setMentor(savedStudy, secondaryMentor.getId(), 2);
            } else {
                setMentor(savedStudy, primaryMentor.getId(), 1);
            }
        }
    }

    private List<StudyPlan> convertToStudyPlans(List<StudyApplyPlan> applyPlans, Study study) {
        return applyPlans.stream()
                .map(plan -> {
                    StudyPlan newPlan = new StudyPlan();
                    StudyPlan.StudyPlanId newId = new StudyPlan.StudyPlanId();
                    newId.setStudyId(study.getId());
                    newId.setWeekNum(plan.getId().getWeekNum());
                    newPlan.setId(newId);
                    newPlan.setSection(plan.getSection());
                    newPlan.setContent(plan.getContent());
                    newPlan.setStudy(study);
                    return newPlan;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    protected void setMentor(Study study, Long mentorId, Integer mentorNum) {
        MentorStudy mentorStudy = new MentorStudy();
        User mentor = userRepository.findById(mentorId).orElseThrow(() -> new ForifException(ErrorCode.USER_NOT_FOUND));
        MentorStudy.MentorStudyId mentorStudyId = new MentorStudy.MentorStudyId();
        mentorStudyId.setMentorId(mentorId);
        mentorStudyId.setStudyId(study.getId());
        mentorStudy.setId(mentorStudyId);
        mentorStudy.setMentorNum(mentorNum);
        mentorStudy.setStudy(study);
        mentorStudy.setUser(mentor);

        mentorStudy.changeUserAuthLv(mentor);
        mentorStudyRepository.save(mentorStudy);
    }

    private Study createStudyFromApply(StudyApply studyApply, User primaryMentor, User secondaryMentor) {
        Study study = new Study();
        study.setExplanation(studyApply.getExplanation());
        study.setDifficulty(studyApply.getDifficulty());
        study.setEndTime(studyApply.getEndTime());
        study.setName(studyApply.getName());
        study.setPrimaryMentorName(primaryMentor.getName());
        study.setSecondaryMentorName(secondaryMentor != null ? secondaryMentor.getName() : null);
        study.setStartTime(studyApply.getStartTime());
        study.setTag(studyApply.getTag());
        study.setWeekDay(studyApply.getWeekDay());
        study.setOneLiner(studyApply.getOneLiner());
        study.setLocation(studyApply.getLocation());
        study.setActYear(DateUtils.getCurrentYear());
        study.setActSemester(DateUtils.getCurrentSemester());
        return study;
    }

    @Transactional
    protected void setStudyApply(StudyApplyRequest request, StudyApply newStudy) {
        User primaryMentor = userRepository.findById(request.getPrimaryMentorId())
                .orElseThrow(() -> new ForifException(ErrorCode.USER_NOT_FOUND));
        newStudy.setPrimaryMentor(primaryMentor);
        newStudy.setPrimaryMentorId(primaryMentor.getId());
        if(request.getSecondaryMentorId() != null) {
            User secondaryMentor = userRepository.findById(request.getSecondaryMentorId())
                    .orElseThrow(() -> new ForifException(ErrorCode.USER_NOT_FOUND));
            newStudy.setSecondaryMentor(secondaryMentor);
            newStudy.setSecondaryMentorId(secondaryMentor.getId());
        }
        newStudy.setName(request.getName());
        newStudy.setOneLiner(request.getOneLiner());
        newStudy.setExplanation(request.getExplanation());
        newStudy.setWeekDay(request.getWeekDay());
        newStudy.setStartTime(request.getStartTime());
        newStudy.setEndTime(request.getEndTime());
        newStudy.setDifficulty(request.getDifficulty());
        newStudy.setLocation(request.getLocation());
        newStudy.setTag(request.getTag());
        newStudy.setAcceptanceStatus(0);
        newStudy.setActYear(LocalDateTime.now().getYear());
        newStudy.setActSemester(LocalDateTime.now().getMonthValue() / 7 + 1);

        List<StudyApplyPlan> studyPlans = request.getStudyApplyPlans().stream()
                .map(planRequest -> {
                    StudyApplyPlan plan = new StudyApplyPlan();
                    StudyApplyPlan.StudyApplyPlanId planId = new StudyApplyPlan.StudyApplyPlanId();
                    planId.setApplyId(newStudy.getId());
                    planId.setWeekNum(request.getStudyApplyPlans().indexOf(planRequest) + 1);
                    plan.setId(planId);
                    plan.setSection(planRequest.getSection());
                    plan.setContent(planRequest.getContent());
                    plan.setStudyApply(newStudy); // 연관 관계 설정
                    return plan;
                })
                .collect(Collectors.toList());

        newStudy.setStudyApplyPlans(studyPlans);
        studyApplyRepository.save(newStudy);
    }

}
