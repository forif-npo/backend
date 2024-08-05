package forif.univ_hanyang.study.controller;

import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.study.dto.request.StudyRequest;
import forif.univ_hanyang.study.dto.response.AllStudyInfoResponse;
import forif.univ_hanyang.study.dto.response.StudyInfoResponse;
import forif.univ_hanyang.study.dto.response.StudyNameResponse;
import forif.univ_hanyang.study.entity.Study;
import forif.univ_hanyang.study.service.StudyService;
import forif.univ_hanyang.user.dto.response.StudyMemberResponse;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.service.UserService;
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

    @GetMapping
    public List<AllStudyInfoResponse> getAllStudiesInfo(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "semester") Integer semester
    ) {
        List<Study> studies = studyService.getAllStudiesInfo(year, semester);
        return studyService.convertToStudyInfoResponse(studies, year, semester);
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<StudyInfoResponse> getStudyInfo(
            @PathVariable Integer studyId
    ) {
        // 성공 시 200 OK 상태 코드와 함께 응답
        return new ResponseEntity<>(studyService.getStudyInfo(studyId),HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping("/{studyId}/users")
    public ResponseEntity<List<StudyMemberResponse>> getStudyMembers(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer studyId
    ) {
        User mentor = userService.validateUserExist(token);
        List<StudyMemberResponse> studyMemberList = studyService.getStudyMembers(mentor, studyId);

        return new ResponseEntity<>(studyMemberList,HttpStatus.OK);
    }

    @RequireJWT
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStudy(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id,
            @RequestBody StudyRequest request) {
        User user = userService.validateUserExist(token);

        studyService.updateStudy(user, id, request);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequireJWT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudy(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        User user = userService.validateUserExist(token);

        studyService.deleteStudy(user, id);

        // 성공 시 200 상태 코드 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequireJWT
    @DeleteMapping("/{studyId}/users/{userId}")
    public ResponseEntity<Void> deleteUserFromStudy(
            @PathVariable Integer studyId,
            @PathVariable Integer userId) {
        studyService.deleteUserFromStudy(studyId, userId);

        // 성공 시 200 No OK 상태 코드 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

