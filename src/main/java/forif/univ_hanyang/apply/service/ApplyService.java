package forif.univ_hanyang.apply.service;

import forif.univ_hanyang.apply.dto.request.AcceptRequest;
import forif.univ_hanyang.apply.dto.request.ApplyRequest;
import forif.univ_hanyang.apply.dto.request.IsPaidRequest;
import forif.univ_hanyang.apply.dto.response.AppliedStudyResponse;
import forif.univ_hanyang.apply.dto.response.MyApplicationResponse;
import forif.univ_hanyang.apply.dto.response.RankedStudyResponse;
import forif.univ_hanyang.apply.dto.response.UnpaidUserResponse;
import forif.univ_hanyang.apply.entity.Apply;
import forif.univ_hanyang.apply.entity.ApplyStatus;
import forif.univ_hanyang.apply.repository.ApplyRepository;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.repository.StudyRepository;
import forif.univ_hanyang.user.domain.StudyUser;
import forif.univ_hanyang.user.domain.User;
import forif.univ_hanyang.user.repository.StudyUserRepository;
import forif.univ_hanyang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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


@Service
@Getter
@RequiredArgsConstructor
public class ApplyService {
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyUserRepository studyUserRepository;
    private final ApplyRepository applyRepository;

    @Transactional
    public void applyStudy(ApplyRequest request, User user) {
        Study study1 = studyRepository.findById(request.getPrimaryStudy()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디가 존재하지 않습니다. ID: " + request.getPrimaryStudy()));

        if (study1.getPrimaryMentorName().equals(user.getName()) || (study1.getSecondaryMentorName() != null && study1.getSecondaryMentorName().equals(user.getName())))
            throw new IllegalArgumentException("자신의 스터디 입니다.");

        // 2순위 스터디 미참여가 아닐 시에만 예외 검사
        if (request.getSecondaryStudy() != null) {
            Study study2 = studyRepository.findById(request.getSecondaryStudy()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디가 존재하지 않습니다. ID: " + request.getSecondaryStudy()));
            if (study2.getPrimaryMentorName().equals(user.getName()) || (study2.getSecondaryMentorName() != null && study2.getSecondaryMentorName().equals(user.getName())))
                throw new IllegalArgumentException("자신의 스터디 입니다.");
        }

        if (applyRepository.findByApplierId(user.getId()).isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 지원서를 접수한 유저입니다. 학번: " + user.getId());

        Apply apply = new Apply();
        apply.setApplierId(user.getId());
        apply.setPrimaryStudy(request.getPrimaryStudy());
        apply.setSecondaryStudy(request.getSecondaryStudy());
        apply.setPrimaryIntro(request.getPrimaryIntro());
        apply.setSecondaryIntro(request.getSecondaryIntro());
        apply.setApplyPath(request.getApplyPath());
        // 모든 지원서 초기엔 N으로 설정 (합격 후 금액 납부 원칙)
        apply.setPayYn("N");
        apply.setPrimaryStatus(ApplyStatus.대기);
        apply.setSecondaryStatus(ApplyStatus.대기);
        apply.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString());

        applyRepository.save(apply);
    }


    /**
     * 있는지 여부를 판단하기 때문에, 없을 시 예외 처리하지 않고 null 값으로 대체
     */
    @Transactional(readOnly = true)
    public MyApplicationResponse getUserApplication(User user) {
        Apply apply = applyRepository.findByApplierId(user.getId()).orElse(null);
        if (apply == null) {
            return null;
        }
        MyApplicationResponse response = new MyApplicationResponse();
        AppliedStudyResponse primaryStudy = new AppliedStudyResponse();
        AppliedStudyResponse secondaryStudy = new AppliedStudyResponse();

        primaryStudy.setId(apply.getPrimaryStudy());
        primaryStudy.setName(studyRepository.findById(apply.getPrimaryStudy()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디가 없습니다.")).getName());
        primaryStudy.setIntroduction(apply.getPrimaryIntro());
        primaryStudy.setStatus(apply.getPrimaryStatus().toString());

        if(apply.getSecondaryStudy() == null) {
            response.setPrimaryStudy(primaryStudy);
            response.setSecondaryStudy(null);
            response.setApplyPath(apply.getApplyPath());
            response.setTimestamp(apply.getApplyDate());
            return response;
        }
        secondaryStudy.setId(apply.getSecondaryStudy());
        secondaryStudy.setName(studyRepository.findById(apply.getSecondaryStudy()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디가 없습니다.")).getName());
        secondaryStudy.setIntroduction(apply.getSecondaryIntro());
        secondaryStudy.setStatus(apply.getSecondaryStatus().toString());

        response.setPrimaryStudy(primaryStudy);
        response.setSecondaryStudy(secondaryStudy);
        response.setApplyPath(apply.getApplyPath());
        response.setTimestamp(apply.getApplyDate());

        return response;
    }


    @Transactional
    public Apply patchApplication(User user, ApplyRequest request) throws IllegalAccessException, InvocationTargetException {
        Apply apply = applyRepository.findByApplierId(user.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "지원서가 없습니다."));
        // request 객체에서 apply 객체로 null이 아닌 필드만 복사
        BeanUtils.copyProperties(apply, request);
        apply.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString());

        applyRepository.save(apply);

        return apply;
    }

    @Transactional
    public void deleteApplication(User user) {
        applyRepository.deleteByApplierId(user.getId());
    }


    @Transactional
    public void acceptApplication(User mentor, AcceptRequest request) {
        Study study = studyRepository.findById(request.getStudyId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디가 없습니다."));
        if (!mentor.getName().equals(study.getPrimaryMentorName()) && !mentor.getName().equals(study.getSecondaryMentorName()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "멘토가 아닙니다.");

        Set<Integer> ApplierIds = request.getApplierIds();
        for (Integer applierId : ApplierIds) {
            Apply apply = applyRepository.findByApplierId(applierId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "지원서를 찾을 수 없습니다. applierId: " + applierId));

            User user = userRepository.findById(applierId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다. Id: " + applierId));

            if (request.getApplyStatus().equals("거절")) continue;

            // 이미 1순위 스터디가 승낙이면, 2순위는 고려안함.
            if (apply.getPrimaryStatus() == ApplyStatus.승낙) continue;

            // 2순위 스터디를 이미 승낙받은 상황에서, 1순위 스터디가 승낙된다면, 2순위 스터디는 제거
            if (apply.getSecondaryStatus().equals(ApplyStatus.승낙)) {
                if (apply.getPrimaryStudy().equals(study.getId())) {
                    Study secondStudy = studyRepository.findById(apply.getSecondaryStudy()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디 없음"));
                    studyUserRepository.deleteById_StudyIdAndId_UserId(secondStudy.getId(), applierId);
                }
            }

            // 1순위 스터디인지 2순위 스터디인지 구분
            if (apply.getPrimaryStudy().equals(study.getId())) apply.setPrimaryStatus(ApplyStatus.승낙);
            else apply.setSecondaryStatus(ApplyStatus.승낙);


            // UserStudyId 인스턴스 생성
            StudyUser.StudyUserId StudyUserId = new StudyUser.StudyUserId();
            StudyUserId.setStudyId(study.getId());
            StudyUserId.setUserId(user.getId());

            // StudyUser 엔티티를 생성하여 스터디와 유저를 연결
            StudyUser StudyUser = new StudyUser();
            StudyUser.setId(StudyUserId);
            StudyUser.setStudy(study);
            StudyUser.setUser(user);

            studyUserRepository.save(StudyUser);
        }
    }

    @Transactional(readOnly = true)
    public List<UnpaidUserResponse> getUnpaidUsers() {
        return applyRepository.findAllByPayYn("N").stream()
                .map(apply -> new UnpaidUserResponse(apply.getApplierId(), getUserName(apply), apply.getPrimaryStudy(), apply.getSecondaryStudy(), getUserPhoneNumber(apply.getApplierId())))
                .collect(Collectors.toList());
    }


    /**
     * @param apply 지원서를 변수로 받음
     * @return userName 지원서 상의 유저 이름
     */
    @Transactional(readOnly = true)
    public String getUserName(Apply apply) {
        return userRepository.findById(apply.getApplierId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저가 없습니다.")).getName();
    }

    /**
     * @param userId 유저의 학번
     * @return phoneNumber 유저의 전화번호
     */
    @Transactional(readOnly = true)
    public String getUserPhoneNumber(Integer userId) {
        return userRepository.findById(userId).map(User::getPhoneNumber).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Map<String, List<RankedStudyResponse>> getAllApplicationsOfStudy(Integer studyId, User user) {
        if (user.getAuthLv() < 3) throw new IllegalStateException("잘못된 접근입니다.");

        studyRepository.findById(studyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디가 없습니다."));

        List<Apply> allApplies = applyRepository.findAll(); // 모든 Apply 엔티티를 가져옴

        List<Apply> primaryStudies = allApplies.stream()
                .filter(apply -> studyId.equals(apply.getPrimaryStudy()))
                .toList();
        List<Apply> secondaryStudies = allApplies.stream()
                .filter(apply -> studyId.equals(apply.getSecondaryStudy()))
                .toList();

        Map<String, List<RankedStudyResponse>> applications = new TreeMap<>();

        List<RankedStudyResponse> studyResponses1 = new LinkedList<>();

        for (Apply apply : primaryStudies) {
            RankedStudyResponse rankedStudyResponse = new RankedStudyResponse();
            rankedStudyResponse.setName(getUserName(apply));
            rankedStudyResponse.setIntro(apply.getPrimaryIntro());
            rankedStudyResponse.setId(apply.getApplierId());
            rankedStudyResponse.setApplyPath(apply.getApplyPath());
            rankedStudyResponse.setPhoneNumber(getUserPhoneNumber(apply.getApplierId()));

            studyResponses1.add(rankedStudyResponse);
        }

        applications.put("first", studyResponses1);

        List<RankedStudyResponse> studyResponses2 = new LinkedList<>();

        for (Apply apply : secondaryStudies) {
            RankedStudyResponse rankedStudyResponse = new RankedStudyResponse();
            rankedStudyResponse.setName(getUserName(apply));
            rankedStudyResponse.setIntro(apply.getSecondaryIntro());
            rankedStudyResponse.setId(apply.getApplierId());
            rankedStudyResponse.setApplyPath(apply.getApplyPath());
            rankedStudyResponse.setPhoneNumber(getUserPhoneNumber(apply.getApplierId()));

            studyResponses2.add(rankedStudyResponse);
        }

        applications.put("second", studyResponses2);

        return applications;
    }

    @Transactional
    public void patchIsPaid(User user, IsPaidRequest request) {
        if (user.getAuthLv() == 1) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        Set<Integer> applierIds = request.getApplierIds();
        for (Integer applierId : applierIds) {
            Apply apply = applyRepository.findByApplierId(applierId).orElseThrow(() -> new EntityNotFoundException("잘못된 지원자 아이디입니다. Id: " + applierId));
            apply.setPayYn(request.getPayYn());
        }
    }


    @Transactional
    public void deleteAllApplications(User user) {
        if (user.getAuthLv() != 4) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        applyRepository.deleteAll();
    }

}
