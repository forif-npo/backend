package forif.univ_hanyang.domain.apply.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class IsPaidRequest {
    private Set<Long> applierIds;
    private Integer payStatus;
}
