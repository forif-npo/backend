package forif.univ_hanyang.auth.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String department;
    private Integer id;
    private String phoneNumber;
}
