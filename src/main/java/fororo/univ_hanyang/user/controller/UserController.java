package fororo.univ_hanyang.user.controller;

import fororo.univ_hanyang.jwt.RequireJWT;
import fororo.univ_hanyang.user.dto.request.UserInfoRequest;
import fororo.univ_hanyang.user.dto.request.UserPatchRequest;
import fororo.univ_hanyang.user.dto.response.UserInfoResponse;
import fororo.univ_hanyang.user.entity.User;
import fororo.univ_hanyang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("연결 성공", HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<User> signIn(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    @RequireJWT
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(
            @RequestHeader("Authorization") String token,
            @RequestBody UserInfoRequest userInfoRequest
    ) {
        userService.validateSignUp(token);
        User user = userService.setUser(userInfoRequest, token);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @RequireJWT
    @PatchMapping("/user")
    public ResponseEntity<User> patchUser(
            @RequestHeader("Authorization") String token,
            @RequestBody UserPatchRequest request
    ) {
        User user = userService.validateUserExist(token);

        return new ResponseEntity<>(userService.patchUser(request ,user),HttpStatus.OK);
    }

    @RequireJWT
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        userService.deleteUser(user);

        return new ResponseEntity<>("회원 삭제 성공", HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUser(
            @RequestHeader("Authorization") String token
    ){
        User user = userService.validateUserExist(token);
        return ResponseEntity.ok(userService.getUserInfo(user));
    }
}


