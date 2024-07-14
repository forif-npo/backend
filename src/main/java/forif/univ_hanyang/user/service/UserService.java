package forif.univ_hanyang.user.service;

import forif.univ_hanyang.apply.repository.ApplyRepository;
import forif.univ_hanyang.clubInfo.service.ClubInfoService;
import forif.univ_hanyang.jwt.JWTValidator;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.repository.StudyRepository;
import forif.univ_hanyang.user.dto.request.UserInfoRequest;
import forif.univ_hanyang.user.dto.request.UserPatchRequest;
import forif.univ_hanyang.user.dto.response.AllUserInfoResponse;
import forif.univ_hanyang.user.dto.response.TotalUserNumberResponse;
import forif.univ_hanyang.user.dto.response.UserInfoResponse;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.entity.UserAuthorization;
import forif.univ_hanyang.user.entity.StudyUser;
import forif.univ_hanyang.user.repository.UserRepository;
import forif.univ_hanyang.user.repository.StudyUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JWTValidator jwtValidator;
    private final StudyUserRepository studyUserRepository;
    private final StudyRepository studyRepository;
    private final ApplyRepository applyRepository;
    private final ClubInfoService clubInfoService;

    public User setUser(UserInfoRequest userInfoRequest, String token) {

        // 요청으로부터 받은 이름
        Integer id = userInfoRequest.getId();
        String name = userInfoRequest.getName();
        String department = userInfoRequest.getDepartment();

        // JWT 토큰 검증을 통해 ID 획득
        String[] inf = jwtValidator.validateToken(token);


        // 사용자 정보 설정
        User user = new User();

        user.setName(name);
        user.setDepartment(department);
        user.setImage(inf[1]);
        user.setId(id);
        user.setEmail(inf[0]);
        user.setPhoneNumber(userInfoRequest.getPhoneNumber());
        user.setUserAuthorization(UserAuthorization.회원);

        // 사용자 정보 저장
        userRepository.save(user);

        return user;
    }


    public User patchUser(UserPatchRequest request, User user) throws IllegalAccessException, InvocationTargetException {
        if (user.getUserAuthorization().equals(UserAuthorization.회원)
                || user.getUserAuthorization().equals(UserAuthorization.멘토))
            throw new IllegalArgumentException("권한이 없습니다.");
        User targetUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        // request 객체에서 user 객체로 null이 아닌 필드만 복사
        BeanUtils.copyProperties(targetUser, request);

        userRepository.save(targetUser);

        return targetUser;
    }


    public void deleteUser(User user) {
        Integer id = user.getId();
        // 연관된 스터디까지 제거 (수강한 스터디)
        studyUserRepository.deleteAllById_UserId(id);

        // 지원서 제거
        applyRepository.deleteByApplierId(id);

        userRepository.deleteById(id);
    }

    // 중복된 회원 존재하는지 검증하는 메소드
    public void validateSignUp(String token) {

        //요청으러 부터 email 추출
        String[] inf = jwtValidator.validateToken(token);

        // 이메일로 사용자 조회
        Optional<User> optionalStudent = userRepository.findByEmail(inf[0]);
        // email이 등록되어 있으면 예외 처리
        if (optionalStudent.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 가입된 사용자입니다.");
        }
    }

    // 토큰 값의 id 에 해당하는 회원 존재하는지 검증하는 메소드
    public User validateUserExist(String token) {

        // JWT 토큰 검증을 통해 ID 획득
        String[] inf = jwtValidator.validateToken(token);

        return userRepository.findByEmail(inf[0])
                .orElseThrow(() -> new EntityNotFoundException("유저가 없습니다."));
    }

    // controller 에서 validate 과정 거친 후 유저 인포 불러오는 메소드
    public UserInfoResponse getUserInfo(User user) {
        StudyUser StudyUser = studyUserRepository.findRecentStudyUserById_UserId(user.getId()).orElse(null);

        Study myStudy = studyRepository.findByMentorIdAndClubId(user.getId(), clubInfoService.makeClubID()).orElse(null);

        Integer studyId;
        if (myStudy == null) {
            studyId = 0;
        } else {
            studyId = myStudy.getId();
        }
        // 스터디 경험이 있을 시
        if (StudyUser != null) {
            Study recentStudy = studyRepository.findById(StudyUser.getId().getStudyId())
                    .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));

            Set<Integer> studyIds = new HashSet<>();
            // 유저와 연관된 스터디 모두 찾기
            List<StudyUser> userStudies = studyUserRepository.findAllById_UserId(user.getId());
            Integer recentStudyId = recentStudy.getId();

            for (StudyUser userStudy1 : userStudies) {
                Study study = studyRepository.findById(userStudy1.getId().getStudyId())
                        .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));
                // 만약 최근 스터디가 아니라면, 수료한 스터디로 간주, 다 찾아오기
                if (!study.getClubId().equals(recentStudy.getClubId()))
                    studyIds.add(recentStudyId);
            }
            return UserInfoResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .image(user.getImage())
                    .department(user.getDepartment())
                    .userAuthorization(user.getUserAuthorization().toString())
                    .currentStudyId(recentStudyId)
                    .phoneNumber(user.getPhoneNumber())
                    .passedStudyId(studyIds)
                    .myStudy(studyId)
                    .build();
        }
        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .image(user.getImage())
                .department(user.getDepartment())
                .userAuthorization(user.getUserAuthorization().toString())
                .currentStudyId(null)
                .phoneNumber(user.getPhoneNumber())
                .passedStudyId(null)
                .myStudy(studyId)
                .build();

    }


    public List<AllUserInfoResponse> getAllUsersInfo(User admin)
    {
        if (!admin.getUserAuthorization().equals(UserAuthorization.관리자))
            throw new IllegalArgumentException("권한이 없습니다.");

        List<User> users = userRepository.findAll();
        List<AllUserInfoResponse> allUserInfoResponses = new ArrayList<>();
        for (User user : users) {
            AllUserInfoResponse allUserInfoResponse = new AllUserInfoResponse();
            allUserInfoResponse.setName(user.getName());
            allUserInfoResponse.setId(user.getId());
            allUserInfoResponse.setEmail(user.getEmail());
            allUserInfoResponse.setDepartment(user.getDepartment());
            allUserInfoResponse.setPhoneNumber(user.getPhoneNumber());
            allUserInfoResponse.setImage(user.getImage());

            List<StudyUser> userStudies = studyUserRepository.findAllById_UserId(user.getId());
            allUserInfoResponse.setPayment(!userStudies.isEmpty());
            allUserInfoResponses.add(allUserInfoResponse);
        }
        return allUserInfoResponses;
    }

    public TotalUserNumberResponse getTotalUserNumber(){
        TotalUserNumberResponse totalUserNumberResponse = new TotalUserNumberResponse();
        totalUserNumberResponse.setUserNumber(userRepository.findAll().size());

        return totalUserNumberResponse;
    }
}
