package forif.univ_hanyang.domain.apply.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import forif.univ_hanyang.domain.user.entity.User;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user_apply")
public class Apply {

    @Embeddable
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class ApplyId implements Serializable {
        @Column(name = "applier_id", nullable = false, columnDefinition = "BIGINT")
        private Long applierId;

        @Column(name = "apply_year", nullable = false)
        private Integer applyYear;

        @Column(name = "apply_semester", nullable = false)
        private Integer applySemester;
    }

    @EmbeddedId
    private ApplyId id;

    @OneToOne
    @MapsId("applierId")
    @JoinColumn(name = "applier_id")
    private User applier;

    private Integer primaryStudy;
    private Integer secondaryStudy;

    @Column(length = 2000)
    private String primaryIntro;

    @Column(length = 2000)
    private String secondaryIntro;

    @Column(length = 100)
    private String applyPath;

    @Column(length = 50)
    private String applyDate;

    private Integer payStatus;
    private Integer primaryStatus;
    private Integer secondaryStatus;
}
