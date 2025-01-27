package forif.univ_hanyang.post.dto.response;

import forif.univ_hanyang.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FAQResponse {
    private Integer id;
    private Long authorId;
    private String tag;
    private String type;
    private String createdAt;
    private String title;
    private String content;

    public static List<FAQResponse> from(List<Post> postList) {
        List<FAQResponse> faqResponseList = new ArrayList<>();
        for (Post post : postList) {
            FAQResponse faqResponse = new FAQResponse();
            faqResponse.setId(post.getId());
            faqResponse.setAuthorId(post.getUser() != null ? post.getUser().getId() : null);
            faqResponse.setTag(post.getTag());
            faqResponse.setType(post.getType());
            faqResponse.setCreatedAt(post.getCreatedAt());
            faqResponse.setTitle(post.getTitle());
            faqResponse.setContent(post.getContent());

            faqResponseList.add(faqResponse);
        }
        return faqResponseList;
    }
}
