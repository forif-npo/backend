package forif.univ_hanyang.user.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "tb_forif_team")
public class ForifTeam {
    @Embeddable
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class ForifTeamId implements Serializable {
        private Integer actYear;
        private Integer actSemester;
        private Long userId;
    }

    @EmbeddedId
    ForifTeamId id;

    private Integer graduateYear;
    @Column(length = 30)
    private String userTitle;
    @Column(length = 30)
    private String clubDepartment;
    @Column(length = 100)
    private String introTag;
    @Column(length = 100)
    private String selfIntro;
    @Column(length = 300)
    private String profImgUrl;

    @OneToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;
}