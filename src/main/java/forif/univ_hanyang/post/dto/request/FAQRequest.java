package forif.univ_hanyang.post.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FAQRequest {
    private String tag;
    private String title;
    private String content;
}
