package forif.univ_hanyang.post.controller;

import forif.univ_hanyang.post.dto.HackathonResponse;
import forif.univ_hanyang.post.service.HackathonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/hackathons")
public class HackathonController {
    private final HackathonService hackathonService;

    @GetMapping
    public ResponseEntity<List<HackathonResponse>> getHackathons() {
        return new ResponseEntity<>(hackathonService.getHackathons(), HttpStatus.OK);
    }
}
