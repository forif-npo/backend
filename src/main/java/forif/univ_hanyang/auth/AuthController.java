package forif.univ_hanyang.auth;

import forif.univ_hanyang.auth.dto.AccessTokenResponse;
import forif.univ_hanyang.auth.dto.AuthResponse;
import forif.univ_hanyang.auth.dto.SignUpRequest;
import forif.univ_hanyang.auth.dto.TokenRequest;
import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "로그인",
            description = "사용자가 구글 토큰을 이용해서 로그인 함",
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
    @GetMapping("/auth/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestParam String access_token) {
        return new ResponseEntity<>(authService.signIn(access_token), HttpStatus.OK);
    }

    @Operation(
            summary = "회원가입",
            description = "사용자가 구글 토큰을 이용해서 회원가입 함",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD REQUEST"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND"
                    )
            }
    )
    @RequireJWT
    @PostMapping("/auth/sign-up")
    public ResponseEntity<User> signUp(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody SignUpRequest request
    ) {
        User user = authService.setUser(request, accessToken);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(
            summary = "토큰 발급",
            description = "리프레시 토큰을 이용해서 액세스 토큰을 발급함",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND"
                    )
            }
    )
    @PostMapping("/auth/token")
    public ResponseEntity<AccessTokenResponse> getAccessToken(@RequestBody TokenRequest request) {
        String refreshToken = request.getRefresh_token().trim(); // JSON 객체에서 값 추출 및 공백 제거
        AccessTokenResponse accessTokenResponse = authService.getAccessToken(refreshToken);
        return new ResponseEntity<>(accessTokenResponse, HttpStatus.OK);

    }
}
