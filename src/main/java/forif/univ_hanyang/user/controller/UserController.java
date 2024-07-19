package forif.univ_hanyang.user.controller;

import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.user.dto.request.UserPatchRequest;
import forif.univ_hanyang.user.dto.response.AllUserInfoResponse;
import forif.univ_hanyang.user.dto.response.UserInfoResponse;
import forif.univ_hanyang.user.dto.response.UserNumberResponse;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.service.UserService;
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

    @RequireJWT
    @GetMapping("/auth/profile")
    public ResponseEntity<UserInfoResponse> getUser(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        return ResponseEntity.ok(userService.getUserInfo(user));
    }

    @RequireJWT
    @GetMapping("/users")
    public ResponseEntity<List<AllUserInfoResponse>> getAllUsers(
            @RequestHeader("Authorization") String token
    ){
        User admin = userService.validateUserExist(token);
        List<AllUserInfoResponse> allUserInfoResponses = userService.getAllUsersInfo(admin);

        return new ResponseEntity<>(allUserInfoResponses, HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping
    public UserNumberResponse getUserNumber(
            @RequestHeader("Authorization") String token
    ){
        User admin = userService.validateUserExist(token);
        return userService.getUserNumber(admin);
    }



    @Operation(
            summary = "프로필 수정",
            description = "프로필의 이름, 학번, 학과, 전화번호, 사진을 수정함",
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
    @PatchMapping("/auth/profile")
    public ResponseEntity<User> patchUser(
            @RequestHeader("Authorization") String token,
            @RequestBody UserPatchRequest request
    ) throws InvocationTargetException, IllegalAccessException {
        User user = userService.validateUserExist(token);

        return new ResponseEntity<>(userService.patchUser(request, user), HttpStatus.OK);
    }


    @Operation(
            summary = "프로필 삭제",
            description = "프로필을 삭제함",
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
    @DeleteMapping("/auth/profile")
    public ResponseEntity<Void> deleteUser(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        userService.deleteUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}


