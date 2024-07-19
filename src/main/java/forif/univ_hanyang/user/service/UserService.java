package forif.univ_hanyang.user.service;

import forif.univ_hanyang.apply.repository.ApplyRepository;
import forif.univ_hanyang.clubInfo.service.ClubInfoService;
import forif.univ_hanyang.jwt.JwtUtils;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.repository.StudyRepository;
import forif.univ_hanyang.user.dto.request.UserPatchRequest;
import forif.univ_hanyang.user.dto.response.AllUserInfoResponse;
import forif.univ_hanyang.user.dto.response.UserNumberResponse;
import forif.univ_hanyang.user.dto.response.UserInfoResponse;
import forif.univ_hanyang.user.entity.StudyUser;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.entity.UserAuthorization;
import forif.univ_hanyang.user.repository.StudyUserRepository;
import forif.univ_hanyang.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtValidator;
    private final StudyUserRepository studyUserRepository;
    private final StudyRepository studyRepository;
    private final ApplyRepository applyRepository;
    private final ClubInfoService clubInfoService;

    public User patchUser(UserPatchRequest request, User user) throws IllegalAccessException, InvocationTargetException {
        if (user.getUserAuthorization().equals(UserAuthorization.회원)
                || user.getUserAuthorization().equals(UserAuthorization.멘토))
            throw new IllegalArgumentException("권한이 없습니다.");
        User targetUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));

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

    public User validateUserExist(String token) {
        // JWT 토큰 검증
        if (!jwtValidator.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 토큰입니다.");
        }

        // JWT 토큰을 통해 사용자 ID 획득
        String stringId = jwtValidator.getUserIdFromToken(token);
        Integer userId = Integer.valueOf(stringId);

        // 사용자 존재 여부 검증
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"ID에 해당하는 유저가 없습니다."));
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
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디가 없습니다."));

            Set<Integer> studyIds = new HashSet<>();
            // 유저와 연관된 스터디 모두 찾기
            List<StudyUser> userStudies = studyUserRepository.findAllById_UserId(user.getId());
            Integer recentStudyId = recentStudy.getId();

            for (StudyUser userStudy1 : userStudies) {
                Study study = studyRepository.findById(userStudy1.getId().getStudyId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "스터디가 없습니다."));

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

    public UserNumberResponse getUserNumber(User admin){
        if (!admin.getUserAuthorization().equals(UserAuthorization.관리자))
            throw new IllegalArgumentException("권한이 없습니다.");

        UserNumberResponse userNumberResponse = new UserNumberResponse();
        userNumberResponse.setUserNumber(userRepository.findAll().size());

        return userNumberResponse;
    }
}
