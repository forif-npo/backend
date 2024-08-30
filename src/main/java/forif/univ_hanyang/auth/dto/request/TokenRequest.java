package forif.univ_hanyang.auth.dto.request;

import lombok.Data;

@Data
public class TokenRequest {
    private String refresh_token;
}
