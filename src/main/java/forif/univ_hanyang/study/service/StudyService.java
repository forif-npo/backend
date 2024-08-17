package forif.univ_hanyang.study.service;

import forif.univ_hanyang.CustomBeanUtils;
import forif.univ_hanyang.study.dto.request.StudyPatchRequest;
import forif.univ_hanyang.study.dto.response.AllStudyInfoResponse;
import forif.univ_hanyang.study.dto.response.MyCreatedStudiesResponse;
import forif.univ_hanyang.study.dto.response.StudyInfoResponse;
import forif.univ_hanyang.study.dto.response.StudyPlanResponse;
import forif.univ_hanyang.study.entity.MentorStudy;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.entity.StudyPlan;
import forif.univ_hanyang.study.repository.MentorStudyRepository;
import forif.univ_hanyang.study.repository.StudyPlanRepository;
import forif.univ_hanyang.study.repository.StudyRepository;
import forif.univ_hanyang.user.dto.response.StudyMemberResponse;
import forif.univ_hanyang.user.entity.StudyUser;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.repository.StudyUserRepository;
import forif.univ_hanyang.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyUserRepository studyUserRepository;
    private final StudyPlanRepository studyPlanRepository;
    private final MentorStudyRepository mentorStudyRepository;

    @Transactional(readOnly = true)
    public List<Study> getAllStudiesInfo(Integer year, Integer semester) {
        return studyRepository.findAllByActYearAndActSemester(year, semester)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디가 없습니다."));
    }


    @Transactional(readOnly = true)
    public StudyInfoResponse getStudyInfo(Integer studyId) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디를 찾을 수 없습니다."));

        List<StudyPlanResponse> studyPlanResponses = studyPlanRepository.findAllById_StudyIdOrderById_WeekNum(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디의 주간 계획을 찾을 수 없습니다."))
                .stream().map(StudyPlanResponse::from).collect(Collectors.toList());


        return StudyInfoResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .primaryMentorName(study.getPrimaryMentorName())
                .secondaryMentorName(study.getSecondaryMentorName())
                .explanation(study.getExplanation())
                .startTime(study.getStartTime())
                .endTime(study.getEndTime())
                .weekDay(study.getWeekDay())
                .difficulty(study.getDifficulty())
                .image(study.getImage())
                .location(study.getLocation())
                .tag(study.getTag())
                .oneLiner(study.getOneLiner())
                .actYear(study.getActYear())
                .actSemester(study.getActSemester())
                .studyPlans(studyPlanResponses)
                .build();
    }

    public List<StudyMemberResponse> getStudyMembers(User mentor, Integer studyId) {
        if (mentor.getAuthLv() == 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        List<StudyUser> userStudies = studyUserRepository.findAllById_StudyId(studyId);
        List<StudyMemberResponse> userList = new ArrayList<>();
        for (StudyUser StudyUser : userStudies) {
            User member = StudyUser.getUser();
            StudyMemberResponse studyMemberResponse = new StudyMemberResponse();
            studyMemberResponse.setId(member.getId());
            studyMemberResponse.setName(member.getName());
            studyMemberResponse.setDepartment(member.getDepartment());
            studyMemberResponse.setEmail(member.getEmail());
            studyMemberResponse.setPhoneNumber(member.getPhoneNumber());

            userList.add(studyMemberResponse);
        }

        return userList;
    }


    @Transactional
    public void updateStudy(User user, Integer studyId, StudyPatchRequest request) {
        if (user.getAuthLv() == 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        // 기존 스터디를 찾아옴
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디를 찾을 수 없습니다."));

        studyRepository.save(setStudy(study, request));
    }

    @Transactional
    public void deleteStudy(User user, Integer studyId) {
        if (user.getAuthLv() == 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        // studyId로 스터디를 찾음
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디를 찾을 수 없습니다."));
        // 해당 스터디 신청 혹은 수강 중인 유저 모두 삭제
        studyUserRepository.deleteAllById_StudyId(studyId);
        // 주간 계획 모두 삭제
        studyPlanRepository.deleteAllByStudy_Id(studyId);
        // 멘토 스터디 삭제
        mentorStudyRepository.deleteAllById_StudyId(studyId);
        // 스터디 삭제
        studyRepository.delete(study);
    }

    @Transactional
    public void deleteUserFromStudy(Integer studyId, Integer userId) {
        // studyId로 스터디가 존재하는지 검증
        studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디를 찾을 수 없습니다."));
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 유저를 찾을 수 없습니다."));

        StudyUser StudyUser = studyUserRepository.findById_StudyIdAndId_UserId(studyId, userId);
        //유저 삭제
        studyUserRepository.delete(StudyUser);
    }

    @Transactional
    public List<AllStudyInfoResponse> convertToStudyInfoResponse(List<Study> studies, Integer year, Integer semester) {
        List<AllStudyInfoResponse> result = new ArrayList<>();

        for (Study study : studies) {
            AllStudyInfoResponse studyInfoResponse = new AllStudyInfoResponse();
            studyInfoResponse.setId(study.getId());
            studyInfoResponse.setName(study.getName());
            studyInfoResponse.setStartTime(study.getStartTime());
            studyInfoResponse.setEndTime(study.getEndTime());
            studyInfoResponse.setWeekDay(study.getWeekDay());
            studyInfoResponse.setDifficulty(study.getDifficulty());
            studyInfoResponse.setExplanation(study.getExplanation());
            studyInfoResponse.setPrimaryMentorName(study.getPrimaryMentorName());
            studyInfoResponse.setSecondaryMentorName(study.getSecondaryMentorName());
            studyInfoResponse.setImage(study.getImage());
            studyInfoResponse.setTag(study.getTag());
            studyInfoResponse.setActYear(year);
            studyInfoResponse.setActSemester(semester);
            result.add(studyInfoResponse);
        }
        return result;
    }

    @Transactional
    public Study setStudy(Study study, StudyPatchRequest request) {
        try {
            // CustomBeanUtils 을 사용하여 null 이 아닌 속성만 복사
            CustomBeanUtils.copyNonNullProperties(study, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // studyPlans 와 같은 복잡한 속성은 수동으로 처리
        List<StudyPlan> studyPlans = studyPlanRepository.findAllById_StudyIdOrderById_WeekNum(study.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디의 주간 계획을 찾을 수 없습니다."));


        for (int i = 0; i < 15; i++) {
            StudyPlan studyPlan = studyPlans.get(i);
            studyPlan.getId().setStudyId(study.getId());
            studyPlan.getId().setWeekNum(i + 1);
            studyPlan.setSection(request.getStudyPlans().get(i).getSection());
            studyPlan.setContent(request.getStudyPlans().get(i).getContent());
            studyPlan.setStudy(study);
        }
        study.setStudyPlans(studyPlans);

        return study;
    }

    public List<MyCreatedStudiesResponse> getMyCreatedStudies(User user) {
        if (user.getAuthLv() == 1)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        List<MentorStudy> mentorStudies = mentorStudyRepository.findAllById_MentorId(user.getId());
        List<MyCreatedStudiesResponse> result = new ArrayList<>();

        List<Study> studies = mentorStudies.stream().map(MentorStudy::getStudy).toList();

        for (Study study : studies) {
            MyCreatedStudiesResponse myCreatedStudiesResponse = new MyCreatedStudiesResponse();
            myCreatedStudiesResponse.setId(study.getId());
            myCreatedStudiesResponse.setActYear(study.getActYear());
            myCreatedStudiesResponse.setActSemester(study.getActSemester());
            result.add(myCreatedStudiesResponse);
        }

        return result;
    }
}
