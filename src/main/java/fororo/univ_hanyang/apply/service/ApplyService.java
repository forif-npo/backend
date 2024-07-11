package fororo.univ_hanyang.apply.service;

import fororo.univ_hanyang.apply.dto.request.AcceptRequest;
import fororo.univ_hanyang.apply.dto.request.ApplyRequest;
import fororo.univ_hanyang.apply.dto.request.IsPaidRequest;
import fororo.univ_hanyang.apply.dto.response.RankedStudyResponse;
import fororo.univ_hanyang.apply.dto.response.UnpaidUserResponse;
import fororo.univ_hanyang.apply.entity.Apply;
import fororo.univ_hanyang.apply.entity.ApplyStatus;
import fororo.univ_hanyang.apply.repository.ApplyRepository;
import fororo.univ_hanyang.clubInfo.service.ClubInfoService;
import fororo.univ_hanyang.study.entity.Study;
import fororo.univ_hanyang.study.repository.StudyRepository;
import fororo.univ_hanyang.user.entity.User;
import fororo.univ_hanyang.user.entity.UserAuthorization;
import fororo.univ_hanyang.user.entity.UserStudy;
import fororo.univ_hanyang.user.repository.UserRepository;
import fororo.univ_hanyang.user.repository.UserStudyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserStudyRepository userStudyRepository;
    private final ApplyRepository applyRepository;
    private final ClubInfoService clubInfoService;

    @Transactional
    public void applyStudy(ApplyRequest request, User user) {
        Study study1 = studyRepository.findByStudyName(request.getPrimaryStudy())
                .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다. 이름: " + request.getPrimaryStudy()));

        if (study1.getMentorId().equals(user.getId()))
            throw new IllegalArgumentException("자신의 스터디 입니다.");

        // 2순위 스터디 미참여가 아닐 시에만 예외 검사
        if (!request.getSecondaryStudy().equals("미참여")) {
            Study study2 = studyRepository.findByStudyName(request.getSecondaryStudy())
                    .orElseThrow(() -> new EntityNotFoundException("스터디가 존재하지 않습니다. 이름: " + request.getSecondaryStudy()));
            if (study2.getMentorId().equals(user.getId()))
                throw new IllegalArgumentException("자신의 스터디 입니다.");
        }

        if (applyRepository.findByApplierId(user.getId()).isPresent())
            throw new IllegalStateException("이미 지원서를 접수한 유저입니다. 학번: " + user.getId());

        Apply apply = new Apply();
        apply.setApplierId(user.getId());
        apply.setPrimaryStudy(request.getPrimaryStudy());
        apply.setSecondaryStudy(request.getSecondaryStudy());
        apply.setPrimaryIntro(request.getPrimaryIntro());
        apply.setSecondaryIntro(request.getSecondaryIntro());
        apply.setCareer(request.getCareer());
        // 모든 지원서 초기엔 false 로 설정 (합격 후 금액 납부 원칙)
        apply.setIsPaid(false);
        apply.setPrimaryStatus(ApplyStatus.대기);
        apply.setSecondaryStatus(ApplyStatus.대기);
        apply.setDate(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        applyRepository.save(apply);
    }


    @Transactional
    public void acceptApplication(AcceptRequest request) {
        Study study = studyRepository.findByStudyId(request.getStudyId())
                .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));

        Set<Integer> ApplierIds = request.getApplierIds();
        for (Integer applierId : ApplierIds) {
            Apply apply = applyRepository.findByApplierId(applierId)
                    .orElseThrow(() -> new EntityNotFoundException("지원서를 찾을 수 없습니다. applierId: " + applierId));

            User user = userRepository.findById(applierId)
                    .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다. Id: " + applierId));

            if (request.getApplyStatus().equals("거절"))
                continue;

            // 이미 1순위 스터디가 승낙이면, 2순위는 고려안함.
            if (apply.getPrimaryStatus() == ApplyStatus.승낙)
                continue;

            // 2순위 스터디를 이미 승낙받은 상황에서, 1순위 스터디가 승낙된다면, 2순위 스터디는 제거
            if (apply.getSecondaryStatus().equals(ApplyStatus.승낙)) {
                if (apply.getPrimaryStudy().equals(study.getStudyName())) {
                    Study secondStudy = studyRepository.findByStudyName(apply.getSecondaryStudy())
                            .orElseThrow(() -> new EntityNotFoundException("스터디 없음"));
                    userStudyRepository.deleteById_StudyIdAndId_UserId(secondStudy.getStudyId(), applierId);
                }
            }

            // 1순위 스터디인지 2순위 스터디인지 구분
            if (apply.getPrimaryStudy().equals(study.getStudyName()))
                apply.setPrimaryStatus(ApplyStatus.승낙);
            else
                apply.setSecondaryStatus(ApplyStatus.승낙);


            // UserStudyId 인스턴스 생성
            UserStudy.UserStudyId userStudyId = new UserStudy.UserStudyId();
            userStudyId.setStudyId(study.getStudyId());
            userStudyId.setUserId(user.getId());

            // UserStudy 엔티티를 생성하여 스터디와 유저를 연결
            UserStudy userStudy = new UserStudy();
            userStudy.setId(userStudyId);
            userStudy.setStudy(study);
            userStudy.setUser(user);

            userStudyRepository.save(userStudy);
        }
    }

    @Transactional(readOnly = true)
    public List<UnpaidUserResponse> getUnpaidUsers() {
        return applyRepository.findAllByIsPaidFalse().stream()
                .map(apply -> new UnpaidUserResponse(apply.getApplierId(),
                        getUserName(apply), apply.getPrimaryStudy(), apply.getSecondaryStudy(), getUserPhoneNumber(apply.getApplierId())))
                .collect(Collectors.toList());
    }

    /**
     * 있는지 여부를 판단하기 때문에, 없을 시 예외 처리하지 않고 null 값으로 대체
     */
    @Transactional(readOnly = true)
    public Apply getUserApplication(User user) {
        return applyRepository.findByApplierId(user.getId()).orElse(null);
    }

    /**
     * @param apply 지원서를 변수로 받음
     * @return userName 지원서 상의 유저 이름
     */
    @Transactional(readOnly = true)
    public String getUserName(Apply apply) {
        return userRepository.findById(apply.getApplierId())
                .orElseThrow(() -> new EntityNotFoundException("유저가 없습니다."))
                .getUserName();
    }

    /**
     * @param userId 유저의 학번
     * @return phoneNumber 유저의 전화번호
     */
    @Transactional(readOnly = true)
    public String getUserPhoneNumber(Integer userId) {
        return userRepository.findById(userId).map(User::getPhoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Map<String, List<RankedStudyResponse>> getAllApplicationsOfStudy(Integer studyId, User user) {
        if (user.getUserAuthorization().equals(UserAuthorization.멘토)
                || user.getUserAuthorization().equals(UserAuthorization.회원))
            throw new IllegalStateException("잘못된 접근입니다.");

        Study study = studyRepository.findByStudyId(studyId)
                .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));

        String studyName = study.getStudyName();

        List<Apply> primaryStudies = applyRepository.findAllByPrimaryStudy(studyName);
        List<Apply> secondaryStudies = applyRepository.findAllBySecondaryStudy(studyName);

        Map<String, List<RankedStudyResponse>> applications = new TreeMap<>();

        List<RankedStudyResponse> studyResponses1 = new LinkedList<>();

        for (Apply apply : primaryStudies) {
            RankedStudyResponse rankedStudyResponse = new RankedStudyResponse();
            rankedStudyResponse.setName(getUserName(apply));
            rankedStudyResponse.setIntro(apply.getPrimaryIntro());
            rankedStudyResponse.setId(apply.getApplierId());
            rankedStudyResponse.setCareer(apply.getCareer());
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
            rankedStudyResponse.setCareer(apply.getCareer());
            rankedStudyResponse.setPhoneNumber(getUserPhoneNumber(apply.getApplierId()));

            studyResponses2.add(rankedStudyResponse);
        }

        applications.put("second", studyResponses2);

        return applications;
    }

    @Transactional(readOnly = true)
    public Map<String, List<RankedStudyResponse>> getAllApplicationsOfStudyForMentor(User user) {
        if (user.getUserAuthorization().equals(UserAuthorization.회원))
            throw new IllegalArgumentException("잘못된 접근입니다.");

        Study study = studyRepository.findByMentorIdAndClubId(user.getId(), clubInfoService.makeClubID())
                .orElse(null);

        if (study == null) {
            throw new EntityNotFoundException("스터디가 없습니다.");
        }
        if (!user.getId().equals(study.getMentorId()))
            throw new IllegalArgumentException("해당 스터디의 멘토가 아닙니다.");

        String studyName = study.getStudyName();

        List<Apply> primaryStudies = applyRepository.findAllByPrimaryStudy(studyName);
        List<Apply> secondaryStudies = applyRepository.findAllBySecondaryStudy(studyName);

        Map<String, List<RankedStudyResponse>> applications = new TreeMap<>();

        List<RankedStudyResponse> studyResponses1 = new LinkedList<>();

        for (Apply apply : primaryStudies) {
            RankedStudyResponse rankedStudyResponse = new RankedStudyResponse();
            rankedStudyResponse.setName(getUserName(apply));
            rankedStudyResponse.setIntro(apply.getPrimaryIntro());
            rankedStudyResponse.setId(apply.getApplierId());
            rankedStudyResponse.setCareer(apply.getCareer());
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
            rankedStudyResponse.setCareer(apply.getCareer());
            rankedStudyResponse.setPhoneNumber(getUserPhoneNumber(apply.getApplierId()));

            studyResponses2.add(rankedStudyResponse);
        }

        applications.put("second", studyResponses2);


        return applications;
    }

    @Transactional
    public void patchIsPaid(User user, IsPaidRequest request) {
        if (user.getUserAuthorization().equals(UserAuthorization.회원))
            throw new IllegalArgumentException("잘못된 접근입니다.");

        Set<Integer> applierIds = request.getApplierIds();
        for (Integer applierId : applierIds) {
            Apply apply = applyRepository.findByApplierId(applierId)
                    .orElseThrow(() -> new EntityNotFoundException("잘못된 지원자 아이디입니다. Id: " + applierId));
            apply.setIsPaid(request.getIsPaid());
        }
    }

    @Transactional
    public void deleteApplication(User user, Integer applierId) {
        if (user.getUserAuthorization().equals(UserAuthorization.회원))
            throw new IllegalArgumentException("권한이 없습니다.");

        applyRepository.deleteByApplierId(applierId);
    }

    @Transactional
    public void deleteAllApplications(User user) {
        if (!user.getUserAuthorization().equals(UserAuthorization.관리자))
            throw new IllegalArgumentException("권한이 없습니다.");

        applyRepository.deleteAll();
    }

}
