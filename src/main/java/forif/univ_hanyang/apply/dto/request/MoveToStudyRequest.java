package forif.univ_hanyang.apply.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MoveToStudyRequest {
    private List<Integer> idList;
}
