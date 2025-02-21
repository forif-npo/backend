package forif.univ_hanyang.apply.entity;

import forif.univ_hanyang.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user_apply")
public class Apply {
    @Id
    private Long applierId; // PK이자 FK

    private Integer applyYear;
    private Integer applySemester;
    @OneToOne
    @MapsId
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
