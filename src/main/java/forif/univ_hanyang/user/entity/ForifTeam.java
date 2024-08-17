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
        private Integer userId;
    }

    @EmbeddedId
    ForifTeamId id;

    private String userName;
    private String userTitle;
    private String clubDepartment;
    private String introTag;
    private String selfIntro;
    private String profImgUrl;

    @OneToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;
}