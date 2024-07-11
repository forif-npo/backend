package fororo.univ_hanyang.user.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AllUserInfoResponse {
    private Integer userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private String department;
    private Boolean payment;
    private String image;
}
