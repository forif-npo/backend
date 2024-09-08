package forif.univ_hanyang.apply.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class IsPaidRequest {
    private Set<Long> applierIds;
    private String payYn;
}
