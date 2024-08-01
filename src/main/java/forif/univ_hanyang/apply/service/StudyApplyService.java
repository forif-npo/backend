package forif.univ_hanyang.apply.service;

import forif.univ_hanyang.apply.dto.request.StudyApplyPlanRequest;
import forif.univ_hanyang.apply.dto.request.StudyApplyRequest;
import forif.univ_hanyang.apply.entity.StudyApply;
import forif.univ_hanyang.apply.entity.StudyApplyPlan;
import forif.univ_hanyang.apply.repository.StudyApplyPlanRepository;
import forif.univ_hanyang.apply.repository.StudyApplyRepository;
import forif.univ_hanyang.study.entity.StudyPlan;
import forif.univ_hanyang.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyApplyService {
    private final StudyApplyRepository studyApplyRepository;
    private final StudyApplyPlanRepository studyApplyPlanRepository;

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
}
