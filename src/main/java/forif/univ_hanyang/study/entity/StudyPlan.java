package forif.univ_hanyang.study.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @EqualsAndHashCode
    public static class StudyPlanId implements Serializable {
        private Integer studyId;
        private Integer weekNum;
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