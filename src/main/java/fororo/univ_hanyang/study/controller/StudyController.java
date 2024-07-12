package fororo.univ_hanyang.study.controller;

import fororo.univ_hanyang.jwt.RequireJWT;
import fororo.univ_hanyang.study.dto.request.StudyInfoRequest;
import fororo.univ_hanyang.study.dto.request.StudyRequest;
import fororo.univ_hanyang.study.dto.response.*;
import fororo.univ_hanyang.study.entity.Study;
import fororo.univ_hanyang.study.service.StudyService;
import fororo.univ_hanyang.user.entity.User;
import fororo.univ_hanyang.user.service.UserService;
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
        return new ResponseEntity<>(studyService.getStudyInfo(new StudyInfoRequest(studyId)),HttpStatus.OK);
    }

    // 주어진 상태의 모든 스터디를 가져옴
    @GetMapping("/status")
    public List<AllStudyInfoResponse> getAllStudiesByStatus(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "semester") Integer semester,
            @RequestParam(value = "status") String status
    ){
        List<Study> studies = studyService.getAllStudiesInfo(year, semester);

        return studyService.convertToStudyInfoResponse(studies, status);
    }

    @RequireJWT
    @GetMapping("/users")
    public ResponseEntity<StudyUserResponse> getStudyOfUser(
            @RequestHeader("Authorization") String token
    ){
        User user = userService.validateUserExist(token);

        return new ResponseEntity<>(studyService.getStudyOfUser(user), HttpStatus.OK);
    }

    /**
     * 관리자용 기능, 추후 옮기기
     * @param id 유저 학번
     * @return userName, studyName
     */
    @GetMapping("/names/{id}")
    public ResponseEntity<StudyNameResponse> getStudyNameOfUser(
            @PathVariable Integer id
    ){
        return new ResponseEntity<>(studyService.getStudyNameOfUser(id), HttpStatus.OK);
    }

    @RequireJWT
    @PostMapping
    public ResponseEntity<Void> saveStudy(
            @RequestBody StudyRequest request,
            @RequestHeader("Authorization") String token) {
        // 유저 검증
        User user = userService.validateUserExist(token);

        studyService.saveStudy(request, user);

        // 저장 성공 시 201 Created 상태 코드로 응답
        return new ResponseEntity<>(HttpStatus.CREATED);
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
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> changeStatus(
            @PathVariable Integer id,
            @RequestParam String status
    ){
        studyService.changeStatus(id, status);
        // 성공 시 204 No Content 상태 코드 응답
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequireJWT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudy(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        User user = userService.validateUserExist(token);

        studyService.deleteStudy(user, id);

        // 성공 시 204 No Content 상태 코드 응답
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequireJWT
    @DeleteMapping("/{studyId}/users/{userId}")
    public ResponseEntity<Void> deleteUserFromStudy(
            @PathVariable Integer studyId,
            @PathVariable Integer userId) {
        studyService.deleteUserFromStudy(studyId, userId);

        // 성공 시 204 No Content 상태 코드 응답
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

