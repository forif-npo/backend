package forif.univ_hanyang.user.dto.request;

import lombok.Data;

@Data
public class UserPatchRequest {
    private String department;
    private String name;
    private String phoneNumber;
}
