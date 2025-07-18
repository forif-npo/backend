package forif.univ_hanyang.domain.user.service;

import forif.univ_hanyang.domain.user.dto.response.ForifTeamResponse;
import forif.univ_hanyang.domain.user.entity.ForifTeam;
import forif.univ_hanyang.domain.user.repository.ForifTeamRepository;
import forif.univ_hanyang.common.exception.ErrorCode;
import forif.univ_hanyang.common.exception.ForifException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForifTeamService {
    private final ForifTeamRepository forifTeamRepository;

    public List<ForifTeamResponse> getForifTeamByActYearAndActSemester(Integer actYear, Integer actSemester) {
        List<ForifTeam> forifTeams = forifTeamRepository.findAllById_ActYearAndId_ActSemester(actYear, actSemester)
                .orElseThrow(() -> new ForifException(ErrorCode.INVALID_SEMESTER_TEAM));

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
                .orElseThrow(() -> new ForifException(ErrorCode.INVALID_STUDENT_ID));

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
