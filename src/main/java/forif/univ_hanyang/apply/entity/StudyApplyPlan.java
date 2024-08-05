package forif.univ_hanyang.apply.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

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
    public static class StudyApplyPlanId implements Serializable {
        private Integer applyId;
        private Integer weekNum;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StudyApplyPlanId that = (StudyApplyPlanId) o;
            return Objects.equals(applyId, that.applyId) && Objects.equals(weekNum, that.weekNum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(applyId, weekNum);
        }
    }

    @EmbeddedId
    private StudyApplyPlanId id;
    private String section;
    private String content;

    @JsonBackReference
    @ManyToOne
    @MapsId("applyId") // 이 필드를 복합 키의 일부로 매핑
    @JoinColumn(name = "apply_id", referencedColumnName = "apply_id", insertable = false, updatable = false)
    private StudyApply studyApply;
}
