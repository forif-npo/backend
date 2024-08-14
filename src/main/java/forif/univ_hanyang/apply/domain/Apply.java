package forif.univ_hanyang.apply.domain;

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
    private Integer applierId;
    private Integer primaryStudy;
    private Integer secondaryStudy;
    private String primaryIntro;
    private String secondaryIntro;
    private String applyPath;
    private String payYn;
    private String applyDate;

    @Enumerated(EnumType.STRING)
    private ApplyStatus primaryStatus;
    @Enumerated(EnumType.STRING)
    private ApplyStatus secondaryStatus;
}