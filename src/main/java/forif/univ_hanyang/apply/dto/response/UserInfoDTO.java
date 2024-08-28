package forif.univ_hanyang.apply.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserInfoDTO {
    private String name;
    private String phoneNumber;
    private String department;
}
