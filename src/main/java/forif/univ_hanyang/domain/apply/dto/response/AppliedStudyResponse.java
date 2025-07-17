package forif.univ_hanyang.domain.apply.dto.response;

import lombok.Data;

@Data
public class AppliedStudyResponse{
    private Integer id;
    private String name;
    private String introduction;
    private Integer status;
}
