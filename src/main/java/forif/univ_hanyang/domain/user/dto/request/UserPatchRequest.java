package forif.univ_hanyang.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequest {
    private String department;
    private String name;
    private String phoneNumber;
}
