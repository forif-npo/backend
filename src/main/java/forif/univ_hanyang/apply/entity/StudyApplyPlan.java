package forif.univ_hanyang.apply.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "tb_study_apply_plan")
public class StudyApplyPlan {
    @Embeddable
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class StudyApplyPlanId implements Serializable {
        private Integer applyId;
        private Integer weekNum;
    }

    @EmbeddedId
    private StudyApplyPlanId id;
    @Column(length = 300)
    private String section;
    @Column(length = 1000)
    private String content;

    @JsonBackReference
    @ManyToOne
    @MapsId("applyId") // 이 필드를 복합 키의 일부로 매핑
    @JoinColumn(name = "apply_id", referencedColumnName = "apply_id", insertable = false, updatable = false)
    private StudyApply studyApply;
}
