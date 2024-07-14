package forif.univ_hanyang.user.dto.response;

import lombok.Data;

@Data
public class AllUserInfoResponse {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    private String department;
    private Boolean payment;
    private String image;
}
