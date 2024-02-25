package fororo.univ_hanyang.user.service;

import fororo.univ_hanyang.apply.repository.ApplyRepository;
import fororo.univ_hanyang.jwt.JWTValidator;
import fororo.univ_hanyang.study.entity.Study;
import fororo.univ_hanyang.study.repository.StudyRepository;
import fororo.univ_hanyang.user.dto.request.UserInfoRequest;
import fororo.univ_hanyang.user.dto.request.UserPatchRequest;
import fororo.univ_hanyang.user.dto.response.UserInfoResponse;
import fororo.univ_hanyang.user.entity.User;
import fororo.univ_hanyang.user.entity.UserAuthorization;
import fororo.univ_hanyang.user.entity.UserStudy;
import fororo.univ_hanyang.user.repository.UserRepository;
import fororo.univ_hanyang.user.repository.UserStudyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final JWTValidator jwtValidator;
    private final UserStudyRepository userStudyRepository;
    private final StudyRepository studyRepository;
    private final ApplyRepository applyRepository;

    public User setUser(UserInfoRequest userInfoRequest, String token) {

        // 요청으로부터 받은 이름
        Integer id = userInfoRequest.getId();
        String userName = userInfoRequest.getUserName();
        String department = userInfoRequest.getDepartment();

        // JWT 토큰 검증을 통해 ID 획득
        String[] inf = jwtValidator.validateToken(token);


        // 사용자 정보 설정
        User user = new User();

        user.setUserName(userName);
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


    public User patchUser(UserPatchRequest request, User user) {
        if(user.getUserAuthorization().equals(UserAuthorization.회원)
                || user.getUserAuthorization().equals(UserAuthorization.멘토))
            throw new IllegalArgumentException("권한이 없습니다.");

        user.setDepartment(request.getDepartment());
        user.setImage(request.getImage());
        user.setId(request.getId());

        // 사용자 정보 저장
        userRepository.save(user);

        return user;
    }


    public void deleteUser(User user) {
        Integer id = user.getId();
        // 연관된 스터디까지 제거 (수강한 스터디)
        userStudyRepository.deleteAllById_UserId(id);

        // 지원서 제거
        applyRepository.deleteByApplierId(id);

        userRepository.deleteById(id);
    }

    // 중복된 회원 존재하는지 검증하는 메소드
    public void validateSignUp(String token) {

        //요청으로 부터 email 추출
        String[] inf = jwtValidator.validateToken(token);

        // 이메일로 사용자 조회
        Optional<User> optionalStudent = userRepository.findByEmail(inf[0]);
        // email이 등록되어 있으면 예외 처리
        if (optionalStudent.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다.");
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
        UserStudy userStudy = userStudyRepository.findRecentUserStudyById_UserId(user.getId()).orElse(null);

        // 스터디 경험이 있을 시
        if (userStudy != null) {
            Study recentStudy = studyRepository.findByStudyId(userStudy.getId().getStudyId())
                    .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));

            Set<Integer> studyIds = new HashSet<>();
            // 유저와 연관된 스터디 모두 찾기
            List<UserStudy> userStudies = userStudyRepository.findAllById_UserId(user.getId());
            Integer recentStudyId = recentStudy.getStudyId();

            for (UserStudy userStudy1 : userStudies) {
                Study study = studyRepository.findByStudyId(userStudy1.getId().getStudyId())
                        .orElseThrow(() -> new EntityNotFoundException("스터디가 없습니다."));
                // 만약 최근 스터디가 아니라면, 수료한 스터디로 간주, 다 찾아오기
                if (!study.getClubId().equals(recentStudy.getClubId()))
                    studyIds.add(recentStudyId);
            }
            return UserInfoResponse.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .userName(user.getUserName())
                    .image(user.getImage())
                    .department(user.getDepartment())
                    .userAuthorization(user.getUserAuthorization().toString())
                    .currentStudyId(recentStudyId)
                    .phoneNumber(user.getPhoneNumber())
                    .passedStudyId(studyIds)
                    .build();
        }
        return UserInfoResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .image(user.getImage())
                .department(user.getDepartment())
                .userAuthorization(user.getUserAuthorization().toString())
                .currentStudyId(null)
                .phoneNumber(user.getPhoneNumber())
                .passedStudyId(null)
                .build();

    }
}
