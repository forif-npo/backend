package forif.univ_hanyang.auth.dto.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String department;
    private Long id;
    private String phoneNumber;
}
