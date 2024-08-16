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
    private String type;
    private String tag;
    private String createdBy;
    private String createdAt;
    private String title;
    private String subtitle;
    private String content;
    private Integer LikeNum;

    public static List<TechResponse> from(List<Post> postList) {
        List<TechResponse> techResponseList = new ArrayList<>();
        for (Post post : postList) {
            TechResponse techResponse = new TechResponse();
            techResponse.setId(post.getId());
            techResponse.setTag(post.getPostTech().getTag());
            techResponse.setType(post.getType());
            techResponse.setCreatedAt(post.getCreatedAt());
            techResponse.setTitle(post.getTitle());
            techResponse.setSubtitle(post.getPostTech().getSubtitle());
            techResponse.setCreatedBy(post.getCreatedBy());
            techResponse.setLikeNum(post.getPostTech().getLikeNum());

            techResponseList.add(techResponse);
        }
        return techResponseList;
    }

    public static TechResponse from(Post post) {
        TechResponse techResponse = new TechResponse();
        techResponse.setId(post.getId());
        techResponse.setTag(post.getPostTech().getTag());
        techResponse.setType(post.getType());
        techResponse.setCreatedAt(post.getCreatedAt());
        techResponse.setTitle(post.getTitle());
        techResponse.setCreatedBy(post.getCreatedBy());
        techResponse.setContent(post.getContent());
        techResponse.setLikeNum(post.getPostTech().getLikeNum());

        return techResponse;
    }
}
