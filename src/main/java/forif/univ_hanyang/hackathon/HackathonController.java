package forif.univ_hanyang.hackathon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "해커톤", description = "해커톤 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/hackathons")
public class HackathonController {
    private final HackathonService hackathonService;

    @Operation(
            summary = "전체 해커톤 조회",
            description = "전체 해커톤을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK")
            }
    )
    @GetMapping
    public ResponseEntity<List<HackathonResponse>> getHackathons() {
        return new ResponseEntity<>(hackathonService.getHackathons(), HttpStatus.OK);
    }
}
