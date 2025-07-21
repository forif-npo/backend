package forif.univ_hanyang.domain.apply.controller;

import forif.univ_hanyang.common.security.jwt.RequireJWT;
import forif.univ_hanyang.common.dto.response.CommonApiResponse;
import forif.univ_hanyang.domain.apply.dto.request.MoveToStudyRequest;
import forif.univ_hanyang.domain.apply.dto.request.StudyApplyRequest;
import forif.univ_hanyang.domain.apply.dto.response.StudyApplyResponse;
import forif.univ_hanyang.domain.apply.service.StudyApplyService;
import forif.univ_hanyang.domain.user.entity.User;
import forif.univ_hanyang.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
                    @ApiResponse(responseCode = "201", description = "CREATED"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
            }
    )
    @RequireJWT
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> createStudyApplication(
            @RequestBody StudyApplyRequest request
    ) {
        studyApplyService.applyStudy(request);
        return ResponseEntity.ok(CommonApiResponse.of(null));
    }

    @Operation(
            summary = "신청된 모든 스터디 조회",
            description = "해당 연도, 학기에 개설 신청된 스터디들을 모두 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
            }
    )
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<StudyApplyResponse>>> getAllAppliedStudiesBy(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer year,
            @RequestParam Integer semester
    ) {
        User admin = userService.validateUserExist(token);
        return ResponseEntity.ok(CommonApiResponse.of(studyApplyService.getAppliedStudies(admin, year, semester)));
    }

    @Operation(
            summary = "신청한 스터디 수정",
            description = "신청된 스터디의 정보를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "CREATED"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
            }
    )
    @PatchMapping("/{applyId}")
    public ResponseEntity<CommonApiResponse<Void>> updateStudyApplication(
            @RequestBody StudyApplyRequest request,
            @PathVariable Integer applyId,
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        studyApplyService.updateStudy(request, user, applyId);

        return ResponseEntity.ok(CommonApiResponse.of(null));
    }


    @Operation(
            summary = "정규 스터디로 이동",
            description = "신청된 스터디를 정규 스터디로 이동합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
                    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
            }
    )
    @PostMapping("/move")
    public ResponseEntity<CommonApiResponse<String>> moveToStudy(
            @RequestHeader("Authorization") String token,
            @RequestBody MoveToStudyRequest request
    ) {
        User admin = userService.validateUserExist(token);
        studyApplyService.moveToStudy(admin, request);
        return ResponseEntity.ok(CommonApiResponse.of("정규 스터디로 이동 완료"));
    }

}
