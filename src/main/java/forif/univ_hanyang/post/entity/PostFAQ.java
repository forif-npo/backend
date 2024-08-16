package forif.univ_hanyang.post.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "tb_post_faq")
public class PostFAQ {
    @Id
    @Column(name = "post_id")
    private Integer id;
    private String tag;

    @OneToOne
    @MapsId
    @JsonBackReference
    @JoinColumn(name = "post_id")
    private Post post;
}
