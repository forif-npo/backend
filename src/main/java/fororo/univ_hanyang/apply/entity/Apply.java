package fororo.univ_hanyang.apply.entity;

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
@Table(name = "apply") // 테이블 이름 지정
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer applyId;
    @Column(name = "applier_id", unique = true)
    private Integer applierId;
    private String primaryStudy;
    private String primaryIntro;
    private String secondaryStudy;
    private String secondaryIntro;
    private String career;
    private Boolean isPaid;

    @Enumerated(EnumType.STRING)
    private ApplyStatus primaryStatus;
    @Enumerated(EnumType.STRING)
    private ApplyStatus secondaryStatus;
}
