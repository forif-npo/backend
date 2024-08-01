package forif.univ_hanyang.apply.controller;

import forif.univ_hanyang.apply.dto.request.StudyApplyRequest;
import forif.univ_hanyang.apply.service.StudyApplyService;
import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "스터디 신청", description = "스터디 신청 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/study-apply")
public class StudyApplyController {
    private final StudyApplyService studyApplyService;
    private final UserService userService;

    @RequireJWT
    @PostMapping
    public ResponseEntity<Void> applyStudy(
            @RequestHeader("Authorization") String token,
            @RequestBody StudyApplyRequest request
    ) {
        userService.validateUserExist(token);

        studyApplyService.applyStudy(request);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
