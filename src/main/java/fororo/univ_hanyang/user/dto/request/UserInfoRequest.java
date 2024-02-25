package fororo.univ_hanyang.user.dto.request;

import lombok.Data;

@Data
public class UserInfoRequest {
    private String userName;
    private String department;
    private Integer id;
    private String phoneNumber;
}