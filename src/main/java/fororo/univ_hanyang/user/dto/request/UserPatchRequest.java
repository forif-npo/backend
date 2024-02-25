package fororo.univ_hanyang.user.dto.request;

import lombok.Data;

@Data
public class UserPatchRequest {
    private String department;
    private Integer id;
    private String image;
}
