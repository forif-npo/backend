package forif.univ_hanyang.apply.controller;

import forif.univ_hanyang.apply.dto.request.MoveToStudyRequest;
import forif.univ_hanyang.apply.dto.request.StudyApplyRequest;
import forif.univ_hanyang.apply.domain.StudyApply;
import forif.univ_hanyang.apply.dto.response.StudyApplyResponse;
import forif.univ_hanyang.apply.service.StudyApplyService;
import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.user.domain.User;
import forif.univ_hanyang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "스터디 신청", description = "스터디 신청 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/study-apply")
public class StudyApplyController {
    private final StudyApplyService studyApplyService;
    private final UserService userService;

    @Operation(
            summary = "스터디 개설 신청",
            description = "스터디 개설을 위한 신청을 합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "CREATED"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED"
                    )
            }
    )
    @RequireJWT
    @PostMapping
    public ResponseEntity<Void> createStudyApplication(
            @RequestBody StudyApplyRequest request
    ) {
        studyApplyService.applyStudy(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "신청된 스터디 조회",
            description = "개설 신청된 스터디들을 모두 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "FORBIDDEN"
                    )
            }
    )
    @RequireJWT
    @GetMapping
    public ResponseEntity<List<StudyApplyResponse>> getAllAppliedStudies(
            @RequestHeader("Authorization") String token
    ) {
        User admin = userService.validateUserExist(token);
        return new ResponseEntity<>(studyApplyService.getAllAppliedStudy(admin), HttpStatus.OK);
    }

    @Operation(
            summary = "정규 스터디로 이동",
            description = "신청된 스터디를 정규 스터디로 이동합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "FORBIDDEN"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT_FOUND"
                    )
            }
    )
    @PostMapping("/move")
    public ResponseEntity<String> moveToStudy(
            @RequestHeader("Authorization") String token,
            @RequestBody MoveToStudyRequest request
    ) {
        User admin = userService.validateUserExist(token);
        studyApplyService.moveToStudy(admin, request);
        return new ResponseEntity<>("정규 스터디로 이동 완료", HttpStatus.OK);
    }

}
