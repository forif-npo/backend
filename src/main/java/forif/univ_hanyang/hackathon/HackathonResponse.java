package forif.univ_hanyang.hackathon;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HackathonResponse {
    private Integer teamId;
    private String teamName;
    private Integer teamRank;
    private String resultUrl;
    private Integer heldYear;
    private Integer heldSemester;

    public static List<HackathonResponse> from(List<Hackathon> hackathonList) {
        List<HackathonResponse> hackathonResponseList = new ArrayList<>();
        for (Hackathon hackathon : hackathonList) {
            HackathonResponse hackathonResponse = new HackathonResponse();
            hackathonResponse.setTeamId(hackathon.getTeamId());
            hackathonResponse.setTeamName(hackathon.getTeamName());
            hackathonResponse.setTeamRank(hackathon.getTeamRank());
            hackathonResponse.setResultUrl(hackathon.getResultUrl());
            hackathonResponse.setHeldYear(hackathon.getHeldYear());
            hackathonResponse.setHeldSemester(hackathon.getHeldSemester());

            hackathonResponseList.add(hackathonResponse);
        }
        return hackathonResponseList;
    }
}
