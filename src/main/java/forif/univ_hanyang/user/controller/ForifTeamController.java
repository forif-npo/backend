package forif.univ_hanyang.user.controller;

import forif.univ_hanyang.user.dto.response.ForifTeamResponse;
import forif.univ_hanyang.user.service.ForifTeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FORIF 운영진", description = "운영진 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/forif-team")
public class ForifTeamController {
    private final ForifTeamService forifTeamService;

    @GetMapping
    public ResponseEntity<List<ForifTeamResponse>> getForifTeamByYearAndSemester(
            @RequestParam Integer year,
            @RequestParam Integer semester
    ) {
        return new ResponseEntity<>(forifTeamService.getForifTeamByActYearAndActSemester(year, semester), HttpStatus.OK);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ForifTeamResponse> getForifTeamById(
//            @PathVariable Integer id
//    ){
//        return new ResponseEntity<>(forifTeamService.getForifTeamById(id), HttpStatus.OK);
//    }
}
