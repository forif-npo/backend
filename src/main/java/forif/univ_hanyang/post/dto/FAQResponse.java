package forif.univ_hanyang.post.dto;

import forif.univ_hanyang.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FAQResponse {
    private Integer id;
    private String tag;
    private String postType;
    private String createdAt;
    private String title;
    private String content;

    public static List<FAQResponse> from(List<Post> postList) {
        List<FAQResponse> faqResponseList = new ArrayList<>();
        for (Post post : postList) {
            FAQResponse faqResponse = new FAQResponse();
            faqResponse.setId(post.getId());
            faqResponse.setTag(post.getPostFAQ().getTag());
            faqResponse.setPostType(post.getPostType());
            faqResponse.setCreatedAt(post.getCreatedAt());
            faqResponse.setTitle(post.getTitle());
            faqResponse.setContent(post.getContent());

            faqResponseList.add(faqResponse);
        }
        return faqResponseList;
    }
}
