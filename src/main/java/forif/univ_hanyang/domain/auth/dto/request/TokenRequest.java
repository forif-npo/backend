package forif.univ_hanyang.domain.auth.dto.request;

import lombok.Data;

@Data
public class TokenRequest {
    private String refresh_token;
}
