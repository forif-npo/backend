package forif.univ_hanyang.user.service;

import forif.univ_hanyang.user.entity.ForifTeam;
import forif.univ_hanyang.user.dto.response.ForifTeamResponse;
import forif.univ_hanyang.user.repository.ForifTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForifTeamService {
    private final ForifTeamRepository forifTeamRepository;

    public List<ForifTeamResponse> getForifTeamByActYearAndActSemester(Integer actYear, Integer actSemester) {
        List<ForifTeam> forifTeams = forifTeamRepository.findAllById_ActYearAndId_ActSemester(actYear, actSemester)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 연도, 학기에 해당하는 운영진이 없습니다."));

        return forifTeams.stream()
                .map(forifTeam -> ForifTeamResponse.builder()
                        .actYear(forifTeam.getId().getActYear())
                        .actSemester(forifTeam.getId().getActSemester())
                        .userTitle(forifTeam.getUserTitle())
                        .clubDepartment(forifTeam.getClubDepartment())
                        .introTag(forifTeam.getIntroTag())
                        .selfIntro(forifTeam.getSelfIntro())
                        .profImgUrl(forifTeam.getProfImgUrl())
                        .user(forifTeam.getUser())
                        .build())
                .toList();
    }

    public ForifTeamResponse getForifTeamById(Long id) {
        ForifTeam forifTeam = forifTeamRepository.findById_UserId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 학번에 해당하는 운영진이 없습니다."));

        return ForifTeamResponse.builder()
                .actYear(forifTeam.getId().getActYear())
                .actSemester(forifTeam.getId().getActSemester())
                .userTitle(forifTeam.getUserTitle())
                .clubDepartment(forifTeam.getClubDepartment())
                .introTag(forifTeam.getIntroTag())
                .selfIntro(forifTeam.getSelfIntro())
                .profImgUrl(forifTeam.getProfImgUrl())
                .user(forifTeam.getUser())
                .build();
    }
}
