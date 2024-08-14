package forif.univ_hanyang.hackathon;

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
    private String teamName;
    private Integer teamRank;
    private String resultUrl;
    private Integer heldYear;
    private Integer heldSemester;
}