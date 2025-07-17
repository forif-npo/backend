package forif.univ_hanyang.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Tag(name = "인증된 유저 정보 반환", description = "토큰을 통해 가져온 유저에 대한 정보")
@Data
public class AuthUserResponse {
    private String email;
    private String name;
    private Long id;
    private String phoneNumber;
    private String department;
    private Integer authLevel;
}
