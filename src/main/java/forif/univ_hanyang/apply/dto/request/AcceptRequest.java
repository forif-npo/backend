package forif.univ_hanyang.apply.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class AcceptRequest {
    private Set<Long> applierIds;
    private String applyStatus;
    private Integer studyId;
}
