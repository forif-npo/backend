package forif.univ_hanyang.domain.post.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementUpdateRequest {
    private String title;
    private String content;
}
