package forif.univ_hanyang.study.controller;

import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.study.dto.request.StudyPatchRequest;
import forif.univ_hanyang.study.dto.response.AllStudyInfoResponse;
import forif.univ_hanyang.study.dto.response.MyCreatedStudiesResponse;
import forif.univ_hanyang.study.dto.response.StudyInfoResponse;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.service.StudyService;
import forif.univ_hanyang.user.dto.response.StudyMemberResponse;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.service.UserService;
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
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "요청 데이터에 대한 정보가 없음"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<AllStudyInfoResponse>> getAllStudiesInfo(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "semester") Integer semester
    ) {
        List<Study> studies = studyService.getStudiesInfo(year, semester);
        return new ResponseEntity<>(studyService.convertToStudyInfoResponse(studies, year, semester), HttpStatus.OK);
    }

    @Operation(
            summary = "스터디 조회",
            description = "해당 스터디의 정보를 조회함",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "요청 데이터에 대한 정보가 없음"
                    )
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
                            description = "요청 데이터에 대한 정보가 없음"
                    )
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
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "UNAUTHORIZED"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "요청 데이터에 대한 정보가 없음"
                    )
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> patchStudy(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id,
            @RequestBody StudyPatchRequest request) {
        User user = userService.validateUserExist(token);

        studyService.updateStudy(user, id, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "스터디 삭제",
            description = "해당 스터디를 삭제함",
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
                            description = "NOT FOUND"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudy(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        User user = userService.validateUserExist(token);

        studyService.deleteStudy(user, id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "스터디의 멘티 삭제",
            description = "해당 스터디의 유저를 삭제함",
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
                            responseCode = "404",
                            description = "NOT FOUND"
                    )
            }
    )
    @RequireJWT
    @DeleteMapping("/{studyId}/users/{userId}")
    public ResponseEntity<Void> deleteUserFromStudy(
            @PathVariable Integer studyId,
            @PathVariable Integer userId) {
        studyService.deleteUserFromStudy(studyId, userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/my-created")
    public ResponseEntity<List<MyCreatedStudiesResponse>> getMyCreatedStudies(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);

        return new ResponseEntity<>(studyService.getMyCreatedStudies(user), HttpStatus.OK);
    }

}

