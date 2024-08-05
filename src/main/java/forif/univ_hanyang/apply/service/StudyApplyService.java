package forif.univ_hanyang.apply.service;

import forif.univ_hanyang.apply.dto.request.MoveToStudyRequest;
import forif.univ_hanyang.apply.dto.request.StudyApplyRequest;
import forif.univ_hanyang.apply.entity.StudyApply;
import forif.univ_hanyang.apply.entity.StudyApplyPlan;
import forif.univ_hanyang.apply.repository.StudyApplyPlanRepository;
import forif.univ_hanyang.apply.repository.StudyApplyRepository;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.entity.StudyPlan;
import forif.univ_hanyang.study.repository.StudyPlanRepository;
import forif.univ_hanyang.study.repository.StudyRepository;
import forif.univ_hanyang.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyApplyService {
    private final StudyApplyRepository studyApplyRepository;
    private final StudyPlanRepository studyPlanRepository;
    private final StudyRepository studyRepository;

    @Transactional
    public void applyStudy(StudyApplyRequest request) {
        StudyApply newStudy = new StudyApply();
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

        List<StudyApplyPlan> studyPlans = request.getStudyPlans().stream()
                .map(planRequest -> {
                    StudyApplyPlan plan = new StudyApplyPlan();
                    StudyApplyPlan.StudyApplyPlanId planId = new StudyApplyPlan.StudyApplyPlanId();
                    planId.setApplyId(newStudy.getId());
                    planId.setWeekNum(request.getStudyPlans().indexOf(planRequest) + 1);
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

    public List<StudyApply> getAllAppliedStudy(User admin) {
        if(admin.getAuthLv() < 3)
            throw new IllegalArgumentException("권한이 없습니다.");
        return studyApplyRepository.findAll();
    }

    @Transactional
    public void moveToStudy(User admin, MoveToStudyRequest request) {
        if (admin.getAuthLv() < 3)
            throw new IllegalArgumentException("권한이 없습니다.");

        List<Integer> idList = request.getIdList();

        for (Integer id : idList) {
            StudyApply studyApply = studyApplyRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디 신청을 찾을 수 없습니다."));

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
            study.setActSemester(LocalDateTime.now().getMonthValue()/ 7  + 1);

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
        }
    }
}
