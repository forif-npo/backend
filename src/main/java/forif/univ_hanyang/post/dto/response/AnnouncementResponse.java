package forif.univ_hanyang.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import forif.univ_hanyang.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnnouncementResponse {
    private Integer id;
    private Long authorId;
    private String type;
    private String createdAt;
    private String title;
    private String content;

    public static AnnouncementResponse from(Post post) {
        AnnouncementResponse announcementResponse = new AnnouncementResponse();
        announcementResponse.setId(post.getId());
        announcementResponse.setAuthorId(post.getUser() != null ? post.getUser().getId() : null);
        announcementResponse.setType(post.getType());
        announcementResponse.setCreatedAt(post.getCreatedAt());
        announcementResponse.setContent(post.getContent());
        announcementResponse.setTitle(post.getTitle());

        return announcementResponse;
    }

    public static List<AnnouncementResponse> from(List<Post> postList) {
        List<AnnouncementResponse> announcementResponseList = new ArrayList<>();

        for (Post post : postList) {
            AnnouncementResponse announcementResponse = new AnnouncementResponse();
            announcementResponse.setId(post.getId());
            announcementResponse.setAuthorId(post.getUser() != null ? post.getUser().getId() : null);
            announcementResponse.setType(post.getType());
            announcementResponse.setCreatedAt(post.getCreatedAt());
            announcementResponse.setTitle(post.getTitle());

            announcementResponseList.add(announcementResponse);
        }
        return announcementResponseList;
    }
}
