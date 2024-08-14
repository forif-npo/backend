package forif.univ_hanyang.auth.dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Tag(name = "인증된 유저 정보 반환", description = "토큰을 통해 가져온 유저에 대한 정보")
@Data
public class AuthUserResponse {
    private String email;
    private String name;
    private Integer id;
    private String phoneNumber;
    private String department;
    private Integer authLevel;
}
