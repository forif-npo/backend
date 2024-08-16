package forif.univ_hanyang.post.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementRequest {
    private String type;
    private String createdBy;
    private String title;
    private String content;
}
