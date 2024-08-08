package forif.univ_hanyang.auth;

import forif.univ_hanyang.auth.dto.*;
import forif.univ_hanyang.jwt.JwtUtils;
import forif.univ_hanyang.user.domain.User;
import forif.univ_hanyang.user.repository.UserRepository;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public String getEmailFromToken(String token) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000) //timeout 시간 조절
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(3000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(3000, TimeUnit.MILLISECONDS)));

        var webClient = WebClient.builder()
                .baseUrl("https://www.googleapis.com/oauth2/v3/userinfo")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        try {
            OAuthResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("access_token", token)
                            .build())
                    .retrieve()
                    .bodyToMono(OAuthResponse.class)
                    .block();

            log.info("Google user email retrieved successfully: {}", Objects.requireNonNull(response).getEmail());
            return response.getEmail();
        } catch (WebClientResponseException e) {
            log.error("Error while retrieving user email: {}", e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while retrieving user email", e);
            throw new RuntimeException("Unexpected error while retrieving user email", e);
        }
    }

    private AuthResponse getAuthResponse(User user) {
        AuthResponse authResponse = new AuthResponse();
        AuthUserResponse authUserResponse = new AuthUserResponse();
        authUserResponse.setEmail(user.getEmail());
        authUserResponse.setName(user.getName());
        authUserResponse.setId(user.getId());
        authUserResponse.setPhoneNumber(user.getPhoneNumber());
        authUserResponse.setDepartment(user.getDepartment());
        authResponse.setUser(authUserResponse);

        authResponse.setAccess_token(jwtUtils.generateAccessToken(user.getId().toString()));
        authResponse.setRefresh_token(jwtUtils.generateRefreshToken(user.getId().toString()));

        return authResponse;
    }

    public AuthResponse signIn(String accessToken) {
        String email = getEmailFromToken(accessToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        return getAuthResponse(user);
    }

    @Transactional
    public User setUser(SignUpRequest request, String access_token) {
        String email = getEmailFromToken(access_token);
        userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."));

        Integer id = request.getId();
        String name = request.getName();
        String department = request.getDepartment();

        // 사용자 정보 설정
        User user = new User();

        user.setName(name);
        user.setDepartment(department);
        user.setId(id);
        user.setEmail(email);
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAuthLv(1);

        // 사용자 정보 저장
        userRepository.save(user);

        return user;
    }

    public AccessTokenResponse getAccessToken(String refresh_token) {
        if (!jwtUtils.validateToken(refresh_token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
        }

        String userId = jwtUtils.getUserIdFromToken(refresh_token);
        Optional<User> user = userRepository.findById(Integer.parseInt(userId));

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");
        }

        if (jwtUtils.isExpired(refresh_token))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");

        AccessTokenResponse accessTokenResponse = new AccessTokenResponse();
        accessTokenResponse.setAccess_token(jwtUtils.generateAccessToken(userId));

        return accessTokenResponse;
    }
}
