package fororo.univ_hanyang.user.dto.request;

import lombok.Data;

@Data
public class UserPatchRequest {
    private Integer id;
    private String department;
    private String userName;
    private String phoneNumber;
    private String image;
}
