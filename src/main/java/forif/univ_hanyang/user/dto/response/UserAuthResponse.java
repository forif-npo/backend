package forif.univ_hanyang.user.dto.response;

import lombok.Data;

@Data
public class UserAuthResponse {
    private String email;
    private String name;
    private Integer id;
    private String phoneNumber;
    private String department;
    private String image;
}
