package forif.univ_hanyang.auth.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String access_token;
    private String refresh_token;
    private AuthUserResponse user;
}
