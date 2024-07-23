package forif.univ_hanyang.auth.dto;

import lombok.Data;

@Data
public class TokenRequest {
    private String refresh_token;
}
