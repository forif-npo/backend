package forif.univ_hanyang.apply.entity;

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
@Table(name = "tb_user_apply") // 테이블 이름 지정
public class Apply {
    @Id
    @Column(name = "applier_id", unique = true)
    private Long applierId;
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
