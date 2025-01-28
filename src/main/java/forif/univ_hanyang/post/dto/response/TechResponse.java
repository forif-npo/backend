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
public class TechResponse {
    private Integer id;
    private Long authorId;
    private String type;
    private String tag;
    private String createdAt;
    private String title;
    private String content;

    public static List<TechResponse> from(List<Post> postList) {
        List<TechResponse> techResponseList = new ArrayList<>();
        for (Post post : postList) {
            TechResponse techResponse = new TechResponse();
            techResponse.setId(post.getId());
            techResponse.setAuthorId(post.getUser() != null ? post.getUser().getId() : null);
            techResponse.setTag(post.getTag());
            techResponse.setType(post.getType());
            techResponse.setCreatedAt(post.getCreatedAt());
            techResponse.setTitle(post.getTitle());

            techResponseList.add(techResponse);
        }
        return techResponseList;
    }

    public static TechResponse from(Post post) {
        TechResponse techResponse = new TechResponse();
        techResponse.setId(post.getId());
        techResponse.setAuthorId(post.getUser() != null ? post.getUser().getId() : null);
        techResponse.setType(post.getType());
        techResponse.setTag(post.getTag());
        techResponse.setCreatedAt(post.getCreatedAt());
        techResponse.setTitle(post.getTitle());
        techResponse.setContent(post.getContent());

        return techResponse;
    }
}
