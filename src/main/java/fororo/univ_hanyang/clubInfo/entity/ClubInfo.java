package fororo.univ_hanyang.clubInfo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "club_info")
public class ClubInfo {
    @Id
    @Column(name = "club_id")
    private Integer clubId;

    @Column(name = "activity_year")
    private Integer activityYear;

    @Column(name = "activity_semester")
    private Integer activitySemester;
}