package forif.univ_hanyang.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AllUserInfoResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String department;
    private String studyName;
}
