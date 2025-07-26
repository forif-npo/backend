package forif.univ_hanyang.domain.study.controller;

import forif.univ_hanyang.common.security.jwt.RequireJWT;
import forif.univ_hanyang.common.dto.response.CommonApiResponse;
import forif.univ_hanyang.domain.study.dto.request.StudyPatchRequest;
import forif.univ_hanyang.domain.study.dto.response.AllStudyInfoResponse;
import forif.univ_hanyang.domain.study.dto.response.MyCreatedStudiesResponse;
import forif.univ_hanyang.domain.study.dto.response.StudyInfoResponse;
import forif.univ_hanyang.domain.study.entity.Study;
import forif.univ_hanyang.domain.study.service.StudyService;
import forif.univ_hanyang.domain.user.dto.response.StudyMemberResponse;
import forif.univ_hanyang.domain.user.entity.User;
import forif.univ_hanyang.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "스터디", description = "스터디 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/studies")
public class StudyController {

    private final StudyService studyService;
    private final UserService userService;

    @Operation(
            summary = "전체 스터디 조회",
            description = "해당 년도와 학기에 해당하는 전체 스터디를 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @GetMapping
    public ResponseEntity<CommonApiResponse<List<AllStudyInfoResponse>>> getAllStudiesInfo(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "semester") Integer semester
    ) {
        List<Study> studies = studyService.getStudiesInfo(year, semester);
        return ResponseEntity.ok(CommonApiResponse.of(studyService.convertToStudyInfoResponse(studies, year, semester)));
    }

    @Operation(
            summary = "스터디 조회",
            description = "해당 스터디의 정보를 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @GetMapping("/{studyId}")
    public ResponseEntity<StudyInfoResponse> getStudyInfo(
            @PathVariable Integer studyId
    ) {
        return new ResponseEntity<>(studyService.getStudyInfo(studyId), HttpStatus.OK);
    }

    @Operation(
            summary = "해당 스터디 부원 전체 조회",
            description = "해당 스터디의 부원들을 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @GetMapping("/{studyId}/users")
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer studyId
    ) {
        User mentor = userService.validateUserExist(token);
        List<StudyMemberResponse> studyMemberList = studyService.getStudyMembers(mentor, studyId);

        return new ResponseEntity<>(studyMemberList, HttpStatus.OK);
    }

    @Operation(
            summary = "스터디 수정",
            description = "해당 스터디의 정보를 수정함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @PatchMapping("/{studyId}")
    public ResponseEntity<Void> patchStudy(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer studyId,
            @RequestBody StudyPatchRequest request) {
        User user = userService.validateUserExist(token);

        studyService.updateStudy(user, studyId, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "스터디 삭제",
            description = "해당 스터디를 삭제함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "403", description = "FORBIDDEN"),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND")
            }
    )
    @DeleteMapping("/{studyId}")
    public ResponseEntity<Void> deleteStudy(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer studyId) {
        User user = userService.validateUserExist(token);

        studyService.deleteStudy(user, studyId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "스터디의 멘티 삭제",
            description = "해당 스터디의 유저를 삭제함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
                    @ApiResponse(responseCode = "404", description = "NOT FOUND")
            }
    )
    @RequireJWT
    @DeleteMapping("/{studyId}/users/{userId}")
    public ResponseEntity<Void> deleteUserFromStudy(
            @PathVariable Integer studyId,
            @PathVariable Long userId) {
        studyService.deleteUserFromStudy(studyId, userId);
    
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Operation(
            summary = "내가 생성한 스터디 조회",
            description = "사용자가 생성한 스터디 목록을 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
            }
    )
    @GetMapping("/my-created")
    public ResponseEntity<List<MyCreatedStudiesResponse>> getMyCreatedStudies(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
    
        return new ResponseEntity<>(studyService.getMyCreatedStudies(user), HttpStatus.OK);
    }

}

