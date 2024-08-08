package forif.univ_hanyang.post.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_post")
public class Post {
    @Id
    @Column(name = "post_id")
    private Integer id;
    @Column(name = "post_type")
    private String type;
    private String createdBy;
    private String createdAt;
    private String title;
    private String content;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private PostFAQ postFAQ;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private PostTech postTech;
}
