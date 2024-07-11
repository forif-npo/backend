package fororo.univ_hanyang.study.service;

import fororo.univ_hanyang.CustomBeanUtils;
import fororo.univ_hanyang.clubInfo.entity.ClubInfo;
import fororo.univ_hanyang.clubInfo.repository.ClubInfoRepository;
import fororo.univ_hanyang.clubInfo.service.ClubInfoService;
import fororo.univ_hanyang.study.dto.request.StudyInfoRequest;
import fororo.univ_hanyang.study.dto.request.StudyRequest;
import fororo.univ_hanyang.study.dto.response.AllStudyInfoResponse;
import fororo.univ_hanyang.study.dto.response.StudyInfoResponse;
import fororo.univ_hanyang.study.dto.response.StudyNameResponse;
import fororo.univ_hanyang.study.dto.response.StudyUserResponse;
import fororo.univ_hanyang.study.entity.*;
import fororo.univ_hanyang.study.repository.StudyRepository;
import fororo.univ_hanyang.study.repository.StudyTagRepository;
import fororo.univ_hanyang.study.repository.TagRepository;
import fororo.univ_hanyang.study.repository.WeeklyPlanRepository;
import fororo.univ_hanyang.user.entity.User;
import fororo.univ_hanyang.user.entity.UserAuthorization;
import fororo.univ_hanyang.user.entity.UserStudy;
import fororo.univ_hanyang.user.repository.UserRepository;
import fororo.univ_hanyang.user.repository.UserStudyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class StudyService {
    
    private final StudyRepository studyRepository;
    private final TagRepository tagRepository;
    private final ClubInfoRepository clubInfoRepository;
    private final UserRepository userRepository;
    private final UserStudyRepository userStudyRepository;
    private final StudyTagRepository studyTagRepository;
    private final ClubInfoService clubInfoService;
    private final WeeklyPlanRepository weeklyPlanRepository;

    @Transactional(readOnly = true)
    public List<Study> getAllStudiesInfo(Integer year, Integer semester) {
        Optional<ClubInfo> optionalClubInfo = clubInfoRepository.findByActivityYearAndActivitySemester(year, semester);
        if (optionalClubInfo.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "연도 또는 학기가 잘못되었습니다.");

        Integer clubInfoId = optionalClubInfo.get().getClubId();
        return studyRepository.findAllByClubId(clubInfoId);
    }


    @Transactional(readOnly = true)
    public StudyInfoResponse getStudyInfo(StudyInfoRequest request) {
        Study study = studyRepository.findById(request.getStudyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 스터디를 찾을 수 없습니다."));

        Map<String, String> tagDetails = study.getTags().stream()
                .map(StudyTag::getTag)
                .collect(Collectors.toMap(Tag::getTagName, Tag::getColor, (v1, v2) -> {
                    // 태그 이름 중복 검사
                    if (v1.equals(v2)) {
                        return v1;
                    } else {
                        throw new IllegalStateException("중복된 태그 이름");
                    }
                }));

        List<String> weeklyPlans = study.getWeeklyPlans().stream()
                .map(WeeklyPlan::getPlan)
                .toList();


        return StudyInfoResponse.builder()
                .studyId(study.getStudyId())
                .studyName(study.getStudyName())
                .mentorId(study.getMentorId())
                .mentorEmail(study.getMentorEmail())
                .mentorName(study.getMentorName())
                .explanation(study.getExplanation())
                .startTime(study.getStartTime())
                .endTime(study.getEndTime())
                .goal(study.getGoal())
                .interview(study.getInterview())
                .weekDay(study.getWeekDay())
                .level(study.getLevel())
                .tags(tagDetails)
                .image(study.getImage())
                .location(study.getLocation())
                .reference(study.getReference())
                .maximumUsers(study.getMaximumUsers())
                .conditions(study.getConditions())
                .weeklyPlans(weeklyPlans)
                .studyType(study.getStudyType().toString())
                .build();
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

        // 권한이 회원일 시엔 대기 상태로 설정
        if (UserAuthorization.회원.equals(user.getUserAuthorization()))
            study.setStudyStatus(StudyStatus.Pending);
        else
            study.setStudyStatus(StudyStatus.Approved);

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
        // 연결된 태그 비워줌
        study.getTags().clear();
        studyTagRepository.deleteByStudy_StudyId(studyId);
        // 해당 스터디 신청 혹은 수강 중인 유저 모두 삭제
        userStudyRepository.deleteAllById_StudyId(studyId);
        // 주간 계획 모두 삭제
        weeklyPlanRepository.deleteAllByStudy_StudyId(studyId);
        // 스터디 삭제
        studyRepository.delete(study);
    }

    @Transactional
    public Set<StudyTag> mapTagNamesToTags(Study study, Set<String> tagNames) {
        Set<StudyTag> studyTags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByTagName(tagName)
                    .orElseThrow(() -> new EntityNotFoundException("태그를 찾을 수 없습니다. tagName: " + tagName));
            StudyTag studyTag = new StudyTag();
            studyTag.setStudy(study);
            studyTag.setTag(tag);

            studyTags.add(studyTag);
        }
        return studyTags;
    }

    @Transactional
    public void deleteUserFromStudy(Integer studyId, Integer userId) {
        // studyId로 스터디가 존재하는지 검증
        studyRepository.findById(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다."));
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        UserStudy userStudy = userStudyRepository.findById_StudyIdAndId_UserId(studyId, userId);
        //유저 삭제
        userStudyRepository.delete(userStudy);
    }

    public void changeStatus(Integer studyId, String status) {
        Study study = studyRepository.findByStudyId(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));

        study.setStudyStatus(StudyStatus.valueOf(status));
    }

    @Transactional
    public List<AllStudyInfoResponse> convertToStudyInfoResponse(List<Study> studies, Integer year, Integer semester) {
        List<AllStudyInfoResponse> result = new ArrayList<>();
        //Tag 가져오기
        for (Study study : studies) {
            AllStudyInfoResponse studyInfoResponse = new AllStudyInfoResponse();

            Map<String, String> tagDetails = study.getTags().stream()
                    .map(StudyTag::getTag)
                    .collect(Collectors.toMap(Tag::getTagName, Tag::getColor, (v1, v2) -> {
                        // 태그 이름 중복 검사
                        if (v1.equals(v2)) {
                            return v1;
                        } else {
                            throw new IllegalStateException("중복된 태그 이름");
                        }
                    }));

            studyInfoResponse.setStudyId(study.getStudyId());
            studyInfoResponse.setStudyName(study.getStudyName());
            studyInfoResponse.setStartTime(study.getStartTime());
            studyInfoResponse.setEndTime(study.getEndTime());
            studyInfoResponse.setWeekDay(study.getWeekDay());
            studyInfoResponse.setLevel(study.getLevel());
            studyInfoResponse.setExplanation(study.getExplanation());
            studyInfoResponse.setMentorName(study.getMentorName());
            studyInfoResponse.setInterview(study.getInterview());
            studyInfoResponse.setImage(study.getImage());
            studyInfoResponse.setStudyType(study.getStudyType().toString());
            studyInfoResponse.setTags(tagDetails);
            studyInfoResponse.setStudyStatus(study.getStudyStatus().toString());
            studyInfoResponse.setYear(year);
            studyInfoResponse.setSemester(semester);
            result.add(studyInfoResponse);
        }
        return result;
    }

    @Transactional
    public List<AllStudyInfoResponse> convertToStudyInfoResponse(List<Study> studies, String status) {
        List<AllStudyInfoResponse> result = new ArrayList<>();
        //Tag 가져오기
        for (Study study : studies) {
            // 스터디 상태가 주어진 상태와 같은 지 확인 후 같지 않다면 그 스터디는 스킵
            if (!StudyStatus.valueOf(status).equals(study.getStudyStatus()))
                continue;
            AllStudyInfoResponse studyInfoResponse = new AllStudyInfoResponse();
            Map<String, String> tagDetails = study.getTags().stream()
                    .map(StudyTag::getTag)
                    .collect(Collectors.toMap(Tag::getTagName, Tag::getColor, (v1, v2) -> {
                        // 태그 이름 중복 검사
                        if (v1.equals(v2)) {
                            return v1;
                        } else {
                            throw new IllegalStateException("중복된 태그 이름");
                        }
                    }));


            studyInfoResponse.setStudyId(study.getStudyId());
            studyInfoResponse.setStudyName(study.getStudyName());
            studyInfoResponse.setStartTime(study.getStartTime());
            studyInfoResponse.setEndTime(study.getEndTime());
            studyInfoResponse.setWeekDay(study.getWeekDay());
            studyInfoResponse.setLevel(study.getLevel());
            studyInfoResponse.setMentorName(study.getMentorName());
            studyInfoResponse.setInterview(study.getInterview());
            studyInfoResponse.setImage(study.getImage());
            studyInfoResponse.setStudyType(study.getStudyType().toString());
            studyInfoResponse.setTags(tagDetails);
            result.add(studyInfoResponse);
        }
        return result;
    }

    /**
     * 간단한 메서드라서 dto 안쓰고 Map 으로 구현함
     */
    @Transactional(readOnly = true)
    public Map<String, List<String>> getStudyNames(List<Study> studies) {
        Map<String, List<String>> result = new HashMap<>();

        for (Study study : studies) {
            String studyType = study.getStudyType().toString();
            String studyName = study.getStudyName();

            // 해당 스터디 타입에 대한 리스트를 가져오거나 없다면 새로 생성
            List<String> studyList = result.computeIfAbsent(studyType, k -> new ArrayList<>());

            // 스터디 이름을 리스트에 추가
            studyList.add(studyName);
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

        study.setStudyName(request.getStudyName());
        study.setMentorId(request.getMentorId());
        study.setClubId(clubInfoService.makeClubID());
        study.setMentorEmail(request.getMentorEmail());
        study.setMentorName(request.getMentorName());
        study.setStartTime(request.getStartTime());
        study.setEndTime(request.getEndTime());
        study.setInterview(request.getInterview());
        study.setWeekDay(request.getWeekDay());
        study.setGoal(request.getGoal());
        study.setLevel(request.getLevel());
        study.setExplanation(request.getExplanation());
        study.setImage(request.getImage());
        study.setLocation(request.getLocation());
        study.setMaximumUsers(request.getMaximumUsers());
        study.setReference(request.getReference());
        study.setWeeklyPlans(weeklyPlans);
        study.setConditions(request.getConditions());
        study.setStudyType(StudyType.valueOf(request.getStudyType()));

        study.getTags().clear();
        study.setTags(mapTagNamesToTags(study, request.getTag()));

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
        List<WeeklyPlan> weeklyPlans = weeklyPlanRepository.findAllByStudy_StudyIdOrderByPlanId(study.getStudyId());
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

        // 열거형과 같은 특별한 처리가 필요한 속성도 수동으로 설정
        if (request.getStudyType() != null)
            study.setStudyType(StudyType.valueOf(request.getStudyType()));

        // 태그 관련 처리
        study.getTags().clear();
        studyTagRepository.deleteByStudy_StudyId(study.getStudyId());
        study.setTags(mapTagNamesToTags(study, request.getTag()));

        return study;
    }

    @Transactional
    public StudyUserResponse getStudyOfUser(User user) {
        UserStudy userStudy = userStudyRepository.findRecentUserStudyById_UserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("수강한 스터디가 없습니다."));
        Study study = studyRepository.findByStudyId(userStudy.getId().getStudyId())
                .orElseThrow(() -> new EntityNotFoundException("해당 스터디가 없습니다."));

        StudyUserResponse studyUserResponse = new StudyUserResponse();
        studyUserResponse.setStudyName(study.getStudyName());
        studyUserResponse.setWeekDay(study.getWeekDay());
        studyUserResponse.setLocation(study.getLocation());
        studyUserResponse.setReference(study.getReference());
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
        Optional<UserStudy> userStudy = userStudyRepository.findRecentUserStudyById_UserId(userId);

        if (userStudy.isPresent()) {
            Study study = studyRepository.findByStudyId(userStudy.get().getId().getStudyId())
                    .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));
            studyNameResponse.setStudyName(study.getStudyName());
        } else {
            studyNameResponse.setStudyName("스터디 없음");
        }

        return studyNameResponse;
    }


}
