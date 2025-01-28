package forif.univ_hanyang.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "tb_post_file")
public class PostFile {
    @Embeddable
    @Getter
    @Setter
    public static class PostFileId implements Serializable {
        @Column(name = "post_id")
        private Integer postId;

        @Column(name = "file_num")
        private Integer fileNum;
    }

    @EmbeddedId
    private PostFileId id;

    @Column(name = "file_type", length = 20)
    private String fileType;

    @Column(name = "file_url", length = 300)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;
}
