package fororo.univ_hanyang.user.controller;

import fororo.univ_hanyang.jwt.RequireJWT;
import fororo.univ_hanyang.user.dto.request.UserInfoRequest;
import fororo.univ_hanyang.user.dto.request.UserPatchRequest;
import fororo.univ_hanyang.user.dto.response.*;
import fororo.univ_hanyang.user.entity.User;
import fororo.univ_hanyang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Tag(name = "사용자", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("연결 성공", HttpStatus.OK);
    }

    @Operation(
            summary = "로그인",
            description = "사용자가 토큰을 이용해서 로그인 함",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND"
                    )
            }
    )
    @PostMapping("/signin")
    public ResponseEntity<User> signIn(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @Operation(
            summary = "회원가입",
            description = "사용자가 토큰을 이용해서 회원가입 함",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND"
                    )
            }
    )
    @RequireJWT
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(
            @RequestHeader("Authorization") String token,
            @RequestBody UserInfoRequest userInfoRequest
    ) {
        userService.validateSignUp(token);
        User user = userService.setUser(userInfoRequest, token);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @RequireJWT
    @PatchMapping("/user")
    public ResponseEntity<User> patchUser(
            @RequestHeader("Authorization") String token,
            @RequestBody UserPatchRequest request
    ) throws InvocationTargetException, IllegalAccessException {
        User user = userService.validateUserExist(token);

        return new ResponseEntity<>(userService.patchUser(request, user), HttpStatus.OK);
    }

    @RequireJWT
    @DeleteMapping("/user")
    public ResponseEntity<UserResponse> deleteUser(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        userService.deleteUser(user);

        UserResponse responseObj = new UserResponse(200, "회원 삭제 성공");

        return new ResponseEntity<>(responseObj, HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUser(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        return ResponseEntity.ok(userService.getUserInfo(user));
    }

    @RequireJWT
    @GetMapping("/user/all")
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer studyId
    ) {
        User mentor = userService.validateUserExist(token);
        List<StudyMemberResponse> studyMemberList = userService.getStudyMembers(mentor, studyId);

        return new ResponseEntity<>(studyMemberList,HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping("/user/allUsers")
    public ResponseEntity<List<AllUserInfoResponse>> getAllUsers(
            @RequestHeader("Authorization") String token
    ){
        User admin = userService.validateUserExist(token);
        List<AllUserInfoResponse> allUserInfoResponses = userService.getAllUsersInfo(admin);

        return new ResponseEntity<>(allUserInfoResponses, HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping("/user/number")
    public ResponseEntity<TotalUserNumberResponse> getTotalUserNumber(
            @RequestHeader("Authorization") String token
    ){
        User admin = userService.validateUserExist(token);
        TotalUserNumberResponse totalUserNumberResponse = userService.getTotalUserNumber();

        return new ResponseEntity<>(totalUserNumberResponse, HttpStatus.OK);
    }
}


