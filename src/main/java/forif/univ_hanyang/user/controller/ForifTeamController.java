package forif.univ_hanyang.user.controller;

import forif.univ_hanyang.user.dto.response.ForifTeamResponse;
import forif.univ_hanyang.user.service.ForifTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "해당 년도, 학기에 해당하는 FORIF 운영진 조회",
            description = "해당 년도, 학기에 해당하는 FORIF 운영진을 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
    @GetMapping
    public ResponseEntity<List<ForifTeamResponse>> getForifTeamByYearAndSemester(
            @RequestParam Integer year,
            @RequestParam Integer semester
    ) {
        return new ResponseEntity<>(forifTeamService.getForifTeamByActYearAndActSemester(year, semester), HttpStatus.OK);
    }

    @Operation(
            summary = "해당 학번에 해당하는 FORIF 운영진 조회",
            description = "해당 학번에 해당하는 FORIF 운영진을 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ForifTeamResponse> getForifTeamById(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(forifTeamService.getForifTeamById(id), HttpStatus.OK);
    }
}
