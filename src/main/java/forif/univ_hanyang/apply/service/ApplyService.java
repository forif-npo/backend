package forif.univ_hanyang.apply.service;

import forif.univ_hanyang.apply.dto.request.AcceptRequest;
import forif.univ_hanyang.apply.dto.request.ApplyRequest;
import forif.univ_hanyang.apply.dto.request.IsPaidRequest;
import forif.univ_hanyang.apply.dto.response.*;
import forif.univ_hanyang.apply.entity.Apply;
import forif.univ_hanyang.apply.repository.ApplyRepository;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.repository.StudyRepository;
import forif.univ_hanyang.user.entity.StudyUser;
import forif.univ_hanyang.user.entity.StudyUserId;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.repository.StudyUserRepository;
import forif.univ_hanyang.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Getter
@RequiredArgsConstructor
public class ApplyService {
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyUserRepository studyUserRepository;
    private final ApplyRepository applyRepository;


    public List<ApplyInfoResponse> getAllApplications(User user) {
        if (user.getAuthLv() < 3) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        List<Apply> applies = applyRepository.findAll();

        // 모든 applier ID와 study ID를 한 번에 수집
        Set<Long> applierIds = applies.stream()
                .map(Apply::getApplierId)
                .collect(Collectors.toSet());

        Set<Integer> studyIds = applies.stream()
                .flatMap(apply -> Stream.of(apply.getPrimaryStudy(), apply.getSecondaryStudy()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 한 번의 쿼리로 모든 사용자 정보를 가져옴
        Map<Long, UserInfoDTO> userInfoMap = getUserInfoBulk(applierIds);

        // 한 번의 쿼리로 모든 스터디 정보를 가져옴
        Map<Integer, String> studyNameMap = getStudyNameBulk(studyIds);

        return applies.stream()
                .map(apply -> {
                    UserInfoDTO userInfo = userInfoMap.get(apply.getApplierId());
                    return new ApplyInfoResponse(
                            apply.getApplierId(),
                            userInfo.getName(),
                            studyNameMap.get(apply.getPrimaryStudy()),
                            studyNameMap.get(apply.getSecondaryStudy()),
                            userInfo.getPhoneNumber(),
                            userInfo.getDepartment(),
                            apply.getApplyPath(),
                            apply.getApplyDate(),
                            apply.getPrimaryStatus(),
                            apply.getSecondaryStatus(),
                            apply.getPayStatus()
                    );
                })
                .collect(Collectors.toList());
    }

    private Map<Long, UserInfoDTO> getUserInfoBulk(Set<Long> userIds) {
        return userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> new UserInfoDTO(user.getName(), user.getPhoneNumber(), user.getDepartment())
                ));
    }

    private Map<Integer, String> getStudyNameBulk(Set<Integer> studyIds) {
        return studyRepository.findAllById(studyIds).stream()
                .collect(Collectors.toMap(
                        Study::getId,
                        Study::getName
                ));
    }

    @Transactional
    public void applyStudy(ApplyRequest request, User user) {
        Optional.of(request.getPrimaryStudy())
                .filter(id -> id != 0)
                .ifPresentOrElse(
                        id -> validateStudy(id, user),
                        () -> validateNoPrimaryStudySelected(request.getPrimaryStudy())
                );

        Optional.ofNullable(request.getSecondaryStudy())
                .filter(id -> id != 0)
                .ifPresent(id -> validateStudy(id, user));

        Apply apply = createApply(request, user);
        applyRepository.save(apply);
    }


    /**
     * 있는지 여부를 판단하기 때문에, 없을 시 예외 처리하지 않고 null 값으로 대체
     */
    public MyApplicationResponse getUserApplication(User user, Integer year, Integer semester) {
        Apply apply = applyRepository.findByApplierIdAndApplyYearAndApplySemester(user.getId(), year, semester).orElse(null);
        if (apply == null) {
            return null;
        }
        MyApplicationResponse response = new MyApplicationResponse();
        AppliedStudyResponse primaryStudy = new AppliedStudyResponse();
        AppliedStudyResponse secondaryStudy = new AppliedStudyResponse();

        primaryStudy.setId(apply.getPrimaryStudy());
        primaryStudy.setName(studyRepository.findById(apply.getPrimaryStudy()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "스터디가 없습니다.")).getName());
        primaryStudy.setIntroduction(apply.getPrimaryIntro());
        primaryStudy.setStatus(apply.getPrimaryStatus());

        if (apply.getSecondaryStudy() == null) {
            response.setPrimaryStudy(primaryStudy);
            response.setSecondaryStudy(null);
            response.setApplyPath(apply.getApplyPath());
            response.setTimestamp(apply.getApplyDate());
            return response;
        }
        secondaryStudy.setId(apply.getSecondaryStudy());
        secondaryStudy.setName(studyRepository.findById(apply.getSecondaryStudy()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "스터디가 없습니다.")).getName());
        secondaryStudy.setIntroduction(apply.getSecondaryIntro());
        secondaryStudy.setStatus(apply.getSecondaryStatus());

        response.setPrimaryStudy(primaryStudy);
        response.setSecondaryStudy(secondaryStudy);
        response.setApplyPath(apply.getApplyPath());
        response.setTimestamp(apply.getApplyDate());

        return response;
    }


    @Transactional
    public Apply patchApplication(User user, ApplyRequest request) throws IllegalAccessException, InvocationTargetException {
        Apply apply = applyRepository.findByApplierId(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원서가 없습니다."));
        // request 객체에서 apply 객체로 null이 아닌 필드만 복사
        BeanUtils.copyProperties(apply, request);
        if (request.getSecondaryStudy() == null)
            apply.setSecondaryStatus(null);
        else
            apply.setSecondaryStatus(0);
        apply.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString());

        applyRepository.save(apply);

        return apply;
    }

    @Transactional
    public void deleteApplication(User user) {
        applyRepository.deleteByApplierId(user.getId());
    }


    @Transactional
    public void acceptApplications(User mentor, AcceptRequest request) {
        Study study = validateMentorAndStudy(mentor, request);

        Set<Long> applierIds = request.getApplierIds();
        for (Long applierId : applierIds) {
            processApplier(applierId, study);
        }
    }

    @Transactional(readOnly = true)
    public List<UserPaymentStatusResponse> getUnpaidUsers() {
        List<Apply> applies = applyRepository.findAll();

        // 모든 applier ID를 한 번에 수집
        Set<Long> applierIds = applies.stream()
                .map(Apply::getApplierId)
                .collect(Collectors.toSet());

        // 한 번의 쿼리로 모든 사용자 정보를 가져옴
        Map<Long, UserInfoDTO> userInfoMap = getUserInfoBulk(applierIds);

        return applies.stream()
                .filter(apply -> apply.getPayStatus().equals(0))
                .flatMap(apply -> { // flatMap을 사용하여 각 신청(Apply)에 대해 0개 또는 1개의 응답을 생성
                    UserInfoDTO userInfo = userInfoMap.get(apply.getApplierId());
                    List<UserPaymentStatusResponse> responses = new ArrayList<>();
                    if (apply.getPrimaryStatus().equals(1)) {
                        responses.add(createResponse(apply, userInfo, apply.getPrimaryStudy(), true));
                    } else if (apply.getSecondaryStatus().equals(1)) {
                        responses.add(createResponse(apply, userInfo, apply.getSecondaryStudy(), false));
                    }
                    return responses.stream();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserPaymentStatusResponse> getPaidUsers() {
        List<Apply> applies = applyRepository.findAll();

        // 모든 applier ID를 한 번에 수집
        Set<Long> applierIds = applies.stream()
                .map(Apply::getApplierId)
                .collect(Collectors.toSet());

        // 한 번의 쿼리로 모든 사용자 정보를 가져옴
        Map<Long, UserInfoDTO> userInfoMap = getUserInfoBulk(applierIds);

        return applies.stream()
                .filter(apply -> apply.getPayStatus().equals(1))
                .flatMap(apply -> { // flatMap을 사용하여 각 신청(Apply)에 대해 0개 또는 1개의 응답을 생성
                    UserInfoDTO userInfo = userInfoMap.get(apply.getApplierId());
                    List<UserPaymentStatusResponse> responses = new ArrayList<>();
                    if (apply.getPrimaryStatus().equals(1)) {
                        responses.add(createResponse(apply, userInfo, apply.getPrimaryStudy(), true));
                    } else if (apply.getSecondaryStatus().equals(1)) {
                        responses.add(createResponse(apply, userInfo, apply.getSecondaryStudy(), false));
                    }
                    return responses.stream();
                })
                .collect(Collectors.toList());
    }

    private UserPaymentStatusResponse createResponse(Apply apply, UserInfoDTO userInfo, Integer studyId, boolean isPrimary) {
        String studyType = studyId != 0 ? "정규 스터디" : "자율 스터디";
        return new UserPaymentStatusResponse(
                apply.getApplierId(),
                userInfo.getName(),
                studyId,
                studyType,
                isPrimary ? "1순위" : "2순위",
                userInfo.getPhoneNumber()
        );
    }

    @Transactional(readOnly = true)
    public Map<String, List<ApplyInfoResponse>> getAllApplicationsOfStudy(Integer studyId, User user) {
        if (user.getAuthLv() < 2) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        studyRepository.findById(studyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "스터디가 없습니다."));

        List<Apply> allApplies = applyRepository.findAll(); // 모든 Apply 엔티티를 가져옴

        List<Apply> primaryStudies = allApplies.stream()
                .filter(apply -> studyId.equals(apply.getPrimaryStudy()))
                .toList();
        List<Apply> secondaryStudies = allApplies.stream()
                .filter(apply -> studyId.equals(apply.getSecondaryStudy()))
                .toList();

        Map<String, List<ApplyInfoResponse>> applications = new TreeMap<>();

        List<ApplyInfoResponse> studyResponses1 = new LinkedList<>();

        for (Apply apply : primaryStudies) {
            UserInfoDTO userInfo = getUserInfo(apply.getApplierId());
            ApplyInfoResponse applyInfoResponse = new ApplyInfoResponse();
            applyInfoResponse.setName(userInfo.getName());
            applyInfoResponse.setIntro(apply.getPrimaryIntro());
            applyInfoResponse.setPrimaryStudyName(getStudyName(apply.getPrimaryStudy()));
            applyInfoResponse.setUserId(apply.getApplierId());
            applyInfoResponse.setApplyPath(apply.getApplyPath());
            applyInfoResponse.setDepartment(userInfo.getDepartment());
            applyInfoResponse.setPhoneNumber(userInfo.getPhoneNumber());

            studyResponses1.add(applyInfoResponse);
        }

        applications.put("first", studyResponses1);

        List<ApplyInfoResponse> studyResponses2 = new LinkedList<>();

        for (Apply apply : secondaryStudies) {
            UserInfoDTO userInfo = getUserInfo(apply.getApplierId());
            ApplyInfoResponse applyInfoResponse = new ApplyInfoResponse();
            applyInfoResponse.setName(userInfo.getName());
            applyInfoResponse.setIntro(apply.getSecondaryIntro());
            applyInfoResponse.setSecondaryStudyName(getStudyName(apply.getSecondaryStudy()));
            applyInfoResponse.setUserId(apply.getApplierId());
            applyInfoResponse.setApplyPath(apply.getApplyPath());
            applyInfoResponse.setPhoneNumber(userInfo.getPhoneNumber());

            studyResponses2.add(applyInfoResponse);
        }

        applications.put("second", studyResponses2);

        return applications;
    }

    @Transactional
    public void patchIsPaid(User user, IsPaidRequest request) {
        if (user.getAuthLv() < 3) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        Set<Long> applierIds = request.getApplierIds();
        for (Long applierId : applierIds) {
            Apply apply = applyRepository.findByApplierId(applierId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원서가 없습니다. ID: " + applierId));
            apply.setPayStatus(request.getPayStatus());
        }
    }

    @Transactional
    public void deleteAllApplications(User user) {
        if (user.getAuthLv() != 4) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        applyRepository.deleteAll();
    }

    private void validateNoPrimaryStudySelected(Integer id) {
        // 자율 스터디일 경우 확인 하지 않음.
        if (id == 0)
            return;
        // 1순위 스터디를 선택하지 않은 경우에 대한 처리
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "1순위 스터디를 무조건 선택해야 합니다.");
    }

    private void validateStudy(Integer studyId, User user) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "스터디가 존재하지 않습니다. ID: " + studyId));

        if (isUserMentorOfStudy(study, user)) {
            throw new IllegalArgumentException(study.getName() + "은(는) 자신의 스터디입니다.");
        }
    }

    private boolean isUserMentorOfStudy(Study study, User user) {
        return study.getPrimaryMentorName().equals(user.getName()) ||
                (study.getSecondaryMentorName() != null && study.getSecondaryMentorName().equals(user.getName()));
    }

    private Apply createApply(ApplyRequest request, User user) {
        Apply apply = new Apply();
        apply.setApplierId(user.getId());
        apply.setApplyYear(LocalDateTime.now().getYear());
        apply.setApplySemester(LocalDateTime.now().getMonthValue() <= 6 ? 1 : 2);
        apply.setPrimaryStudy(request.getPrimaryStudy());
        apply.setSecondaryStudy(request.getSecondaryStudy());
        apply.setPrimaryIntro(request.getPrimaryIntro());
        apply.setSecondaryIntro(request.getSecondaryIntro());
        apply.setApplyPath(request.getApplyPath());
        apply.setPayStatus(0);
        apply.setPrimaryStatus(0);
        if (request.getSecondaryStudy() != null) {
            apply.setSecondaryStatus(0);
        }
        apply.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString());
        return apply;
    }

    private Study validateMentorAndStudy(User mentor, AcceptRequest request) {
        if (mentor.getAuthLv() == 1) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        Study study = studyRepository.findById(request.getStudyId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당하는 스터디가 없습니다."));

        if (request.getStudyId() != 0 && !isUserMentorOfStudy(study, mentor)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 스터디의 멘토가 아닙니다.");
        }

        return study;
    }

    private void processApplier(Long applierId, Study study) {
        Apply apply = applyRepository.findByApplierId(applierId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원서를 찾을 수 없습니다. ID: " + applierId));

        User user = userRepository.findById(applierId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저를 찾을 수 없습니다. ID: " + applierId));

        validateApplyForStudy(apply, study);

        if (apply.getPrimaryStatus().equals(1)) {
            return;  // 이미 1순위 스터디가 승낙이면, 2순위는 고려안함.
        }

        handleSecondaryStudy(apply, study, applierId);
        updateApplyStatus(apply, study);
        createAndSaveStudyUser(study, user);
    }

    private void validateApplyForStudy(Apply apply, Study study) {
        if (!Objects.equals(apply.getPrimaryStudy(), study.getId()) && !Objects.equals(apply.getSecondaryStudy(), study.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 스터디에 지원하지 않은 유저입니다.");
        }
    }

    private void handleSecondaryStudy(Apply apply, Study study, Long applierId) {
        if (apply.getSecondaryStudy() != null &&
                apply.getSecondaryStatus().equals(1) &&
                Objects.equals(apply.getPrimaryStudy(), study.getId())) {
            studyUserRepository.deleteById_StudyIdAndId_UserId(apply.getSecondaryStudy(), applierId);
        }
    }

    private void updateApplyStatus(Apply apply, Study study) {
        if (Objects.equals(apply.getPrimaryStudy(), study.getId())) {
            apply.setPrimaryStatus(1);
        } else {
            apply.setSecondaryStatus(1);
        }
    }

    private void createAndSaveStudyUser(Study study, User user) {
        StudyUser studyUser = new StudyUser();
        studyUser.id = new StudyUserId(study.getId(), user.getId());
        studyUser.study = study;
        studyUser.user = user;
        studyUserRepository.save(studyUser);
    }


    private UserInfoDTO getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(user -> new UserInfoDTO(
                        user.getName(),
                        user.getPhoneNumber(),
                        user.getDepartment()
                ))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저가 없습니다."));
    }


    private String getStudyName(Integer studyId) {
        if (studyId == null) return null;
        return studyRepository.findById(studyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "스터디가 없습니다.")).getName();
    }


}
