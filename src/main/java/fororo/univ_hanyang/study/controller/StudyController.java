package fororo.univ_hanyang.study.controller;

import fororo.univ_hanyang.apply.dto.response.ApplyResponse;
import fororo.univ_hanyang.jwt.RequireJWT;
import fororo.univ_hanyang.study.dto.request.StudyCreateRequest;
import fororo.univ_hanyang.study.dto.request.StudyInfoRequest;
import fororo.univ_hanyang.study.dto.request.StudyUpdateRequest;
import fororo.univ_hanyang.study.dto.response.AllStudyInfoResponse;
import fororo.univ_hanyang.study.dto.response.StudyInfoResponse;
import fororo.univ_hanyang.study.dto.response.StudyResponse;
import fororo.univ_hanyang.study.entity.Study;
import fororo.univ_hanyang.study.service.StudyService;
import fororo.univ_hanyang.user.entity.User;
import fororo.univ_hanyang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/studies")
public class StudyController {

    private final StudyService studyService;
    private final UserService userService;

    @GetMapping("/all")
    public List<AllStudyInfoResponse> getAllStudiesInfo(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "semester") Integer semester
    ) {
        List<Study> studies = studyService.getAllStudiesInfo(year, semester);
        return studyService.convertToStudyInfoResponse(studies);
    }

    // 모든 스터디의 이름만 가져옴
    @GetMapping("/name")
    public ResponseEntity<Map<String, List<String>>> getAllStudyNames(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "semester") Integer semester
    ) {
        List<Study> studies = studyService.getAllStudiesInfo(year, semester);

        return new ResponseEntity<>(studyService.getStudyNames(studies), HttpStatus.OK);
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


    @GetMapping
    public ResponseEntity<StudyInfoResponse> getStudyInfo(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "semester") Integer semester,
            @RequestParam(value = "studyId") Integer studyId
    ) {
        // 성공 시 200 OK 상태 코드와 함께 응답
        return new ResponseEntity<>(studyService.getStudyInfo(new StudyInfoRequest(year, semester, studyId)),HttpStatus.OK);
    }

    @RequireJWT
    @PostMapping
    public ResponseEntity<Void> saveStudy(
            @RequestBody StudyCreateRequest request,
            @RequestHeader("Authorization") String token) {
        // 유저 검증
        User user = userService.validateUserExist(token);

        Study createdStudy = studyService.saveStudy(request, user);

        // 생성된 스터디의 ID를 이용하여 URI 생성
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{studyId}")
                .buildAndExpand(createdStudy.getStudyId())
                .toUri();
        // 저장 성공 시 201 Created 상태 코드와 함께 응답
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{studyId}")
    public ResponseEntity<StudyResponse> updateStudy(
            @PathVariable Integer studyId,
            @RequestBody StudyUpdateRequest request) {
        studyService.updateStudy(studyId, request);

        // 성공 메시지 객체 생성
        StudyResponse responseObj = new StudyResponse(200, "정상 작동");

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(responseObj, HttpStatus.OK);
    }

    @DeleteMapping("/{studyId}")
    public ResponseEntity<StudyResponse> deleteStudy(
            @PathVariable Integer studyId) {
        studyService.deleteStudy(studyId);

        // 성공 메시지 객체 생성
        StudyResponse responseObj = new StudyResponse(200, "정상 작동");

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(responseObj, HttpStatus.OK);
    }

    @RequireJWT
    @DeleteMapping("/{studyId}/deleteUser")
    public ResponseEntity<StudyResponse> deleteUserFromStudy(
            @PathVariable Integer studyId,
            @RequestParam Integer userId) {
        studyService.deleteUserFromStudy(studyId, userId);

        // 성공 메시지 객체 생성
        StudyResponse responseObj = new StudyResponse(200, "정상 작동");

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(responseObj, HttpStatus.OK);
    }

    @RequireJWT
    @PatchMapping("/{studyId}/changeStatus")
    public ResponseEntity<StudyResponse> changeStatus(
            @PathVariable Integer studyId,
            @RequestParam String status
    ){
        studyService.changeStatus(studyId, status);

        // 성공 메시지 객체 생성
        StudyResponse responseObj = new StudyResponse(200, "정상 작동");

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(responseObj, HttpStatus.OK);
    }

}

