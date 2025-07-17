package forif.univ_hanyang.domain.hackathon;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HackathonResponse {
    private Integer teamId;
    private String projectName;
    private String resultUrl;
    private Integer heldYear;
    private Integer heldSemester;

    public static List<HackathonResponse> from(List<Hackathon> hackathonList) {
        List<HackathonResponse> hackathonResponseList = new ArrayList<>();
        for (Hackathon hackathon : hackathonList) {
            HackathonResponse hackathonResponse = new HackathonResponse();
            hackathonResponse.setTeamId(hackathon.getTeamId());
            hackathonResponse.setProjectName(hackathon.getProjectName());
            hackathonResponse.setResultUrl(hackathon.getResultUrl());
            hackathonResponse.setHeldYear(hackathon.getHeldYear());
            hackathonResponse.setHeldSemester(hackathon.getHeldSemester());

            hackathonResponseList.add(hackathonResponse);
        }
        return hackathonResponseList;
    }
}
