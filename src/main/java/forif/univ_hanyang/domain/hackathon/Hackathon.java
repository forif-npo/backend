package forif.univ_hanyang.domain.hackathon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "tb_hackathon")
public class Hackathon {
    @Id
    private Integer teamId;
    @Column(length = 30)
    private String projectName;
    @Column(length = 300)
    private String resultUrl;
    private Integer heldYear;
    private Integer heldSemester;
}