package forif.univ_hanyang.domain.apply.controller;

import forif.univ_hanyang.domain.apply.dto.request.AcceptRequest;
import forif.univ_hanyang.domain.apply.dto.request.ApplyRequest;
import forif.univ_hanyang.domain.apply.dto.request.IsPaidRequest;
import forif.univ_hanyang.domain.apply.dto.response.ApplyInfoResponse;
import forif.univ_hanyang.domain.apply.dto.response.MyApplicationResponse;
import forif.univ_hanyang.domain.apply.dto.response.UserPaymentStatusResponse;
import forif.univ_hanyang.domain.apply.entity.Apply;
import forif.univ_hanyang.domain.apply.service.ApplyService;
import forif.univ_hanyang.domain.user.entity.User;
import forif.univ_hanyang.domain.user.service.UserService;
import forif.univ_hanyang.common.dto.response.CommonApiResponse;
import forif.univ_hanyang.common.exception.ErrorCode;
import forif.univ_hanyang.common.exception.ForifException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplyController {
    private final UserService userService;
    private final ApplyService applyService;

    @Operation(
            summary = "모든 지원서 조회",
            description = "모든 지원서를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
            }
    )
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<ApplyInfoResponse>>> getAllApplications(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer year,
            @RequestParam Integer semester
    ) {
        User user = userService.validateUserExist(token);
        return ResponseEntity.ok(CommonApiResponse.of(applyService.getAllApplications(user, year, semester)));
    }

    @Operation(
            summary = "지원서 작성",
            description = "지원서를 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
            }
    )
    @PostMapping
    public ResponseEntity<CommonApiResponse<Void>> applyStudy(
            @RequestHeader("Authorization") String token,
            @RequestBody ApplyRequest request) {
        User user = userService.validateUserExist(token);
        applyService.applyStudy(request, user);
        return ResponseEntity.ok(CommonApiResponse.of(null));
    }

    @Operation(
            summary = "내 지원서 조회",
            description = "내 지원서를 조회합니다., return <?> 형식으로 해서 null일 때와 지원서가 있을 때 다른 객체를 반환하도록 함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<CommonApiResponse<MyApplicationResponse>> getUserApplication(
            @RequestHeader("Authorization") String token,
            @RequestParam Integer year,
            @RequestParam Integer semester
    ) {
        User user = userService.validateUserExist(token);
        MyApplicationResponse application = applyService.getUserApplication(user, year, semester);
        if (application == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonApiResponse.of(null));
        }
        return ResponseEntity.ok(CommonApiResponse.of(application));
    }

    @Operation(
            summary = "지원서 수정",
            description = "지원서를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
            }
    )
    @PatchMapping("/me")
    public ResponseEntity<CommonApiResponse<Apply>> patchApplication(
            @RequestHeader("Authorization") String token,
            @RequestBody ApplyRequest request
    ) throws InvocationTargetException, IllegalAccessException {
        User user = userService.validateUserExist(token);
        return ResponseEntity.ok(CommonApiResponse.of(applyService.patchApplication(user, request)));
    }

    @Operation(
            summary = "지원서 삭제",
            description = "지원서를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "NO CONTENT"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
            }
    )
    @DeleteMapping("/me")
    public ResponseEntity<CommonApiResponse<Void>> deleteApplication(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        applyService.deleteApplication(user);
        return ResponseEntity.ok(CommonApiResponse.of(null));
    }

    @Operation(
            summary = "지원서 수락",
            description = "지원서를 수락합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
            }
    )
    @PostMapping("/accept")
    public ResponseEntity<CommonApiResponse<Void>> acceptApplications(
            @RequestBody AcceptRequest request,
            @RequestHeader("Authorization") String token) {
        User mentor = userService.validateUserExist(token);
        applyService.acceptApplications(mentor, request);
        return ResponseEntity.ok(CommonApiResponse.of(null));
    }

    @Operation(
            summary = "회비 미지불 유저 조회",
            description = "회비를 미지불한 유저들을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
            }
    )
    @GetMapping("/unpaid-users")
    public ResponseEntity<CommonApiResponse<List<UserPaymentStatusResponse>>> getUnpaidUsers(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        if (user.getAuthLv() < 3)
            throw new ForifException(ErrorCode.UNAUTHORIZED_ACCESS);
        return ResponseEntity.ok(CommonApiResponse.of(applyService.getUnpaidUsers()));
    }

    @Operation(
            summary = "회비 지불 유저 조회",
            description = "회비를 지불한 유저들을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
            }
    )
    @GetMapping("/paid-users")
    public ResponseEntity<CommonApiResponse<List<UserPaymentStatusResponse>>> getPaidUsers(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        if (user.getAuthLv() == 1)
            throw new ForifException(ErrorCode.UNAUTHORIZED_ACCESS);
        return ResponseEntity.ok(CommonApiResponse.of(applyService.getPaidUsers()));
    }

    @Operation(
            summary = "특정 스터디의 모든 지원서 조회",
            description = "특정 스터디의 모든 지원서를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND")
            }
    )
    @GetMapping("/{studyId}")
    public ResponseEntity<CommonApiResponse<Map<String, List<ApplyInfoResponse>>>> getAllApplicationsOfStudy(
            @PathVariable Integer studyId,
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        Map<String, List<ApplyInfoResponse>> applications = applyService.getAllApplicationsOfStudy(studyId, user);
        if (applications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonApiResponse.of(null));
        }
        return ResponseEntity.ok(CommonApiResponse.of(applications));
    }

    @Operation(
            summary = "유저의 납부 상태 수정",
            description = "납부 상태를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
            }
    )
    @PatchMapping("/payment-status")
    public ResponseEntity<CommonApiResponse<Void>> patchIsPaid(
            @RequestHeader("Authorization") String token,
            @RequestBody IsPaidRequest request
    ) {
        User user = userService.validateUserExist(token);
        applyService.patchIsPaid(user, request);
        return ResponseEntity.ok(CommonApiResponse.of(null));
    }

    @Operation(
            summary = "모든 지원서 삭제",
            description = "모든 지원서를 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
            }
    )
    @DeleteMapping
    public ResponseEntity<CommonApiResponse<Void>> deleteAllApplications(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        applyService.deleteAllApplications(user);
        return ResponseEntity.ok(CommonApiResponse.of(null));
    }
}
