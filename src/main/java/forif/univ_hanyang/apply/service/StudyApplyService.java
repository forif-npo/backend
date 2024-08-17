package forif.univ_hanyang.apply.service;

import forif.univ_hanyang.apply.entity.StudyApply;
import forif.univ_hanyang.apply.entity.StudyApplyPlan;
import forif.univ_hanyang.apply.dto.request.MoveToStudyRequest;
import forif.univ_hanyang.apply.dto.request.StudyApplyRequest;
import forif.univ_hanyang.apply.dto.response.StudyApplyResponse;
import forif.univ_hanyang.apply.repository.StudyApplyRepository;
import forif.univ_hanyang.study.entity.MentorStudy;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.entity.StudyPlan;
import forif.univ_hanyang.study.repository.MentorStudyRepository;
import forif.univ_hanyang.study.repository.StudyRepository;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<StudyApplyResponse> getAllAppliedStudy(User admin) {
        if (admin.getAuthLv() < 3)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        List<StudyApply> studyApplies = studyApplyRepository.findAll();

        List<StudyApplyResponse> studyApplyResponses = new ArrayList<>();
        for (StudyApply studyApply : studyApplies) {
            User primaryMentor = userRepository.findById(studyApply.getPrimaryMentorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."));

            StudyApplyResponse studyApplyResponse = StudyApplyResponse.from(studyApply);
            studyApplyResponse.setPrimaryMentorEmail(primaryMentor.getEmail());
            studyApplyResponse.setPrimaryMentorPhoneNumber(primaryMentor.getPhoneNumber());
            if(studyApply.getSecondaryMentorId() == null)
                studyApplyResponses.add(studyApplyResponse);
            else {
                User secondaryMentor = userRepository.findById(studyApply.getSecondaryMentorId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."));
                studyApplyResponse.setSecondaryMentorEmail(secondaryMentor.getEmail());
                studyApplyResponse.setSecondaryMentorPhoneNumber(secondaryMentor.getPhoneNumber());
                studyApplyResponses.add(studyApplyResponse);
            }
        }
        return studyApplyResponses;
    }

    @Transactional
    public void updateStudy(StudyApplyRequest request, User admin, Integer applyId) {
        if(admin.getAuthLv() < 3)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        StudyApply studyApply = studyApplyRepository.findById(applyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디 신청을 찾을 수 없습니다."));
        setStudyApply(request, studyApply);
    }

    @Transactional
    public void moveToStudy(User admin, MoveToStudyRequest request) {
        if (admin.getAuthLv() < 3)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        List<Integer> idList = request.getIdList();

        for (Integer id : idList) {
            StudyApply studyApply = studyApplyRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디 신청을 찾을 수 없습니다."));

            // 승인 상태로 변경
            studyApply.setStatus(1);

            Integer studyId = studyRepository.findMaxStudyId().orElse(0) + 1;

            Study study = new Study();
            study.setId(studyId);
            study.setExplanation(studyApply.getExplanation());
            study.setDifficulty(studyApply.getDifficulty());
            study.setEndTime(studyApply.getEndTime());
            study.setName(studyApply.getName());
            study.setPrimaryMentorName(studyApply.getPrimaryMentorName());
            study.setSecondaryMentorName(studyApply.getSecondaryMentorName());
            study.setStartTime(studyApply.getStartTime());
            study.setTag(studyApply.getTag());
            study.setWeekDay(studyApply.getWeekDay());
            study.setOneLiner(studyApply.getOneLiner());
            study.setLocation(studyApply.getLocation());
            study.setActYear(LocalDateTime.now().getYear());
            study.setActSemester(LocalDateTime.now().getMonthValue() / 7 + 1);

            List<StudyPlan> studyPlans = studyApply.getStudyApplyPlans().stream()
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

            study.setStudyPlans(studyPlans);

            studyRepository.save(study);

            if (study.getSecondaryMentorName() != null) {
                setMentor(study, studyApply.getPrimaryMentorId(), 2);
                setMentor(study, studyApply.getSecondaryMentorId(), 2);
                return;
            }
            setMentor(study, studyApply.getPrimaryMentorId(), 1);
        }
    }

    @Transactional
    public void setMentor(Study study, Integer mentorId, Integer mentorNum) {
        MentorStudy mentorStudy = new MentorStudy();
        User mentor = userRepository.findById(mentorId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."));
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

    @Transactional
    public void setStudyApply(StudyApplyRequest request, StudyApply newStudy) {
        newStudy.setName(request.getName());
        newStudy.setPrimaryMentorName(request.getPrimaryMentorName());
        newStudy.setPrimaryMentorId(request.getPrimaryMentorId());
        newStudy.setSecondaryMentorId(request.getSecondaryMentorId());
        newStudy.setSecondaryMentorName(request.getSecondaryMentorName());
        newStudy.setOneLiner(request.getOneLiner());
        newStudy.setExplanation(request.getExplanation());
        newStudy.setWeekDay(request.getWeekDay());
        newStudy.setStartTime(request.getStartTime());
        newStudy.setEndTime(request.getEndTime());
        newStudy.setDifficulty(request.getDifficulty());
        newStudy.setLocation(request.getLocation());
        newStudy.setTag(request.getTag());

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
