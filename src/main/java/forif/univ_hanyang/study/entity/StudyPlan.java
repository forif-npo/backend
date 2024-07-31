package forif.univ_hanyang.study.entity;

import forif.univ_hanyang.user.entity.StudyUser;
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
@Table(name = "tb_study_plan")
public class StudyPlan {
    @Embeddable
    @Getter
    @Setter
    public static class StudyPlanId implements Serializable {
        private Integer studyId;
        private Integer weekNum;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StudyPlanId that = (StudyPlanId) o;
            return Objects.equals(studyId, that.studyId) && Objects.equals(weekNum, that.weekNum);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studyId, weekNum);
        }
    }

    @EmbeddedId
    private StudyPlanId id;
    private String section;
    private String content;

    @ManyToOne
    @MapsId("studyId") // 이 필드를 복합 키의 일부로 매핑
    @JoinColumn(name = "study_id", referencedColumnName = "study_id", insertable = false, updatable = false)
    private Study study;
}