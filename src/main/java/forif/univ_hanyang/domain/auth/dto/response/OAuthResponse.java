package forif.univ_hanyang.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Tag(name = "OAuth 토큰 정보 반환", description = "구글로부터 받은 토큰에 담겨 있는 정보")
@Data
public class OAuthResponse {
    private String email;
}
