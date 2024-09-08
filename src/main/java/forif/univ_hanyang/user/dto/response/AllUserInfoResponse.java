package forif.univ_hanyang.user.dto.response;

import lombok.Data;

@Data
public class AllUserInfoResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String department;
}
