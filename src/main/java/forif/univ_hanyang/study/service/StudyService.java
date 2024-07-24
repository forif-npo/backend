package forif.univ_hanyang.study.service;

import forif.univ_hanyang.CustomBeanUtils;
import forif.univ_hanyang.clubInfo.entity.ClubInfo;
import forif.univ_hanyang.clubInfo.repository.ClubInfoRepository;
import forif.univ_hanyang.clubInfo.service.ClubInfoService;
import forif.univ_hanyang.study.dto.request.StudyInfoRequest;
import forif.univ_hanyang.study.dto.request.StudyRequest;
import forif.univ_hanyang.study.dto.response.AllStudyInfoResponse;
import forif.univ_hanyang.study.dto.response.StudyInfoResponse;
import forif.univ_hanyang.study.dto.response.StudyNameResponse;
import forif.univ_hanyang.study.dto.response.StudyUserResponse;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.entity.WeeklyPlan;
import forif.univ_hanyang.study.repository.StudyRepository;
import forif.univ_hanyang.study.repository.WeeklyPlanRepository;
import forif.univ_hanyang.user.dto.response.StudyMemberResponse;
import forif.univ_hanyang.user.entity.StudyUser;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.entity.UserAuthorization;
import forif.univ_hanyang.user.repository.StudyUserRepository;
import forif.univ_hanyang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class StudyService {
    
    private final StudyRepository studyRepository;
    private final ClubInfoRepository clubInfoRepository;
    private final UserRepository userRepository;
    private final StudyUserRepository studyUserRepository;
    private final ClubInfoService clubInfoService;
    private final WeeklyPlanRepository weeklyPlanRepository;

    @Transactional(readOnly = true)
    public List<Study> getAllStudiesInfo(Integer year, Integer semester) {
        Optional<ClubInfo> optionalClubInfo = clubInfoRepository.findByActivityYearAndActivitySemester(year, semester);
        if (optionalClubInfo.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "연도 또는 학기가 잘못되었습니다.");

        Integer clubInfoId = optionalClubInfo.get().getClubId();
        return studyRepository.findAllByClubIdWithDetails(clubInfoId);
    }


    @Transactional(readOnly = true)
    public StudyInfoResponse getStudyInfo(StudyInfoRequest request) {
        Study study = studyRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디를 찾을 수 없습니다."));

        List<String> weeklyPlans = study.getWeeklyPlans().stream()
                .map(WeeklyPlan::getPlan)
                .toList();


        return StudyInfoResponse.builder()
                .id(study.getId())
                .name(study.getName())
                .mentorId(study.getMentorId())
                .mentorName(study.getMentorName())
                .explanation(study.getExplanation())
                .startTime(study.getStartTime())
                .endTime(study.getEndTime())
                .weekDay(study.getWeekDay())
                .level(study.getLevel())
                .image(study.getImage())
                .location(study.getLocation())
                .tag(study.getTag())
                .weeklyPlans(weeklyPlans)
                .build();
    }

    public List<StudyMemberResponse> getStudyMembers(User mentor, Integer studyId) {
        if (mentor.getUserAuthorization().equals(UserAuthorization.회원))
            throw new IllegalArgumentException("권한이 없습니다.");

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

    /**
     * 스터디를 수기로 등록해야 하므로, 멘토와 유저가 같은 지 검증하지 않음.
     */
    @Transactional
    public void saveStudy(StudyRequest request, User user) {
//        if (!user.getUserName().equals(request.getMentorName()))
//            throw new IllegalArgumentException("학번 혹은 이름이 잘못되었습니다.");

        //유저 권한 체크
        if (user.getUserAuthorization() == null) {
            throw new IllegalArgumentException("스터디 생성 권한이 없습니다.");
        }
        Study study = setStudy(request);

        studyRepository.save(study);
    }


    @Transactional
    public void updateStudy(User user, Integer studyId, StudyRequest request) {
        // 기존 스터디를 찾아옴
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new EntityNotFoundException("해당하는 스터디를 찾을 수 없습니다."));

        if (user.getUserAuthorization().equals(UserAuthorization.회원))
            throw new IllegalArgumentException("권한이 없습니다.");

        studyRepository.save(setStudy(study, request));
    }

    @Transactional
    public void deleteStudy(User user, Integer studyId) {
        if (user.getUserAuthorization().equals(UserAuthorization.회원))
            throw new IllegalArgumentException("권한이 없습니다.");

        // studyId로 스터디를 찾음
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new EntityNotFoundException("해당하는 스터디를 찾을 수 없습니다."));
        // 해당 스터디 신청 혹은 수강 중인 유저 모두 삭제
        studyUserRepository.deleteAllById_StudyId(studyId);
        // 주간 계획 모두 삭제
        weeklyPlanRepository.deleteAllByStudy_Id(studyId);
        // 스터디 삭제
        studyRepository.delete(study);
    }

    @Transactional
    public void deleteUserFromStudy(Integer studyId, Integer userId) {
        // studyId로 스터디가 존재하는지 검증
        studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다."));
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

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
            studyInfoResponse.setLevel(study.getLevel());
            studyInfoResponse.setExplanation(study.getExplanation());
            studyInfoResponse.setMentorName(study.getMentorName());
            studyInfoResponse.setImage(study.getImage());
            studyInfoResponse.setTag(study.getTag());
            studyInfoResponse.setYear(year);
            studyInfoResponse.setSemester(semester);
            result.add(studyInfoResponse);
        }
        return result;
    }

    @Transactional
    public Study setStudy(StudyRequest request) {
        List<WeeklyPlan> weeklyPlans = new ArrayList<>();

        Study study = new Study();

        for (String content : request.getWeeklyPlan()) {
            WeeklyPlan weeklyPlan = new WeeklyPlan();
            weeklyPlan.setPlan(content);
            weeklyPlan.setStudy(study);
            weeklyPlans.add(weeklyPlan);
        }

        study.setName(request.getName());
        study.setMentorId(request.getMentorId());
        study.setClubId(clubInfoService.makeClubID());
        study.setMentorName(request.getMentorName());
        study.setStartTime(request.getStartTime());
        study.setEndTime(request.getEndTime());
        study.setWeekDay(request.getWeekDay());
        study.setLevel(request.getLevel());
        study.setExplanation(request.getExplanation());
        study.setImage(request.getImage());
        study.setLocation(request.getLocation());
        study.setTag(request.getTag());
        study.setWeeklyPlans(weeklyPlans);

        return study;
    }

    @Transactional
    public Study setStudy(Study study, StudyRequest request) {
        try {
            // CustomBeanUtils 을 사용하여 null 이 아닌 속성만 복사
            CustomBeanUtils.copyNonNullProperties(study, request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // weeklyPlans 와 같은 복잡한 속성은 수동으로 처리
        List<WeeklyPlan> weeklyPlans = weeklyPlanRepository.findAllByStudy_IdOrderByPlanId(study.getId());
        List<String> updatedPlans = request.getWeeklyPlan();

        for (int i = 0; i < weeklyPlans.size(); i++) {
            if (i >= updatedPlans.size() || updatedPlans.get(i) == null) {
                continue;
            }
            WeeklyPlan weeklyPlan = weeklyPlans.get(i);
            weeklyPlan.setPlan(updatedPlans.get(i));
            weeklyPlan.setStudy(study);
        }
        study.setWeeklyPlans(weeklyPlans);

        return study;
    }

    @Transactional
    public StudyUserResponse getStudyOfUser(User user) {
        StudyUser StudyUser = studyUserRepository.findRecentStudyUserById_UserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("수강한 스터디가 없습니다."));
        Study study = studyRepository.findById(StudyUser.getId().getStudyId())
                .orElseThrow(() -> new EntityNotFoundException("해당 스터디가 없습니다."));

        StudyUserResponse studyUserResponse = new StudyUserResponse();
        studyUserResponse.setName(study.getName());
        studyUserResponse.setWeekDay(study.getWeekDay());
        studyUserResponse.setLocation(study.getLocation());
        studyUserResponse.setStartTime(study.getStartTime());
        studyUserResponse.setEndTime(study.getEndTime());
        studyUserResponse.setMentorName(study.getMentorName());

        List<String> weeklyPlans = new ArrayList<>();
        for (WeeklyPlan weeklyPlan : study.getWeeklyPlans()) {
            String plan = weeklyPlan.getPlan();
            weeklyPlans.add(plan);
        }
        studyUserResponse.setWeeklyPlans(weeklyPlans);

        return studyUserResponse;
    }

    @Transactional
    public StudyNameResponse getStudyNameOfUser(Integer userId) {
        StudyNameResponse studyNameResponse = new StudyNameResponse();

        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 없습니다."));
        Optional<StudyUser> StudyUser = studyUserRepository.findRecentStudyUserById_UserId(userId);

        if (StudyUser.isPresent()) {
            Study study = studyRepository.findById(StudyUser.get().getId().getStudyId())
                    .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));
            studyNameResponse.setName(study.getName());
        } else {
            studyNameResponse.setName("스터디 없음");
        }

        return studyNameResponse;
    }


}
