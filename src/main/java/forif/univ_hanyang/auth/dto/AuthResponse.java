package forif.univ_hanyang.auth.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private AuthUserResponse user;
}
