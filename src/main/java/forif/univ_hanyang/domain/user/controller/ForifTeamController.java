package forif.univ_hanyang.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import forif.univ_hanyang.domain.user.dto.response.ForifTeamResponse;
import forif.univ_hanyang.domain.user.service.ForifTeamService;
import forif.univ_hanyang.common.dto.response.CommonApiResponse;

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
    public ResponseEntity<CommonApiResponse<List<ForifTeamResponse>>> getForifTeamByYearAndSemester(
            @RequestParam Integer year,
            @RequestParam Integer semester
    ) {
        return ResponseEntity.ok(CommonApiResponse.of(forifTeamService.getForifTeamByActYearAndActSemester(year, semester)));
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
    public ResponseEntity<CommonApiResponse<ForifTeamResponse>> getForifTeamById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(CommonApiResponse.of(forifTeamService.getForifTeamById(id)));
    }
}
