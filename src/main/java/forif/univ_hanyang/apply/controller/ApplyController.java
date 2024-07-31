package forif.univ_hanyang.apply.controller;

import forif.univ_hanyang.apply.dto.request.AcceptRequest;
import forif.univ_hanyang.apply.dto.request.ApplyRequest;
import forif.univ_hanyang.apply.dto.request.IsPaidRequest;
import forif.univ_hanyang.apply.dto.response.ApplyResponse;
import forif.univ_hanyang.apply.dto.response.MyApplicationResponse;
import forif.univ_hanyang.apply.dto.response.RankedStudyResponse;
import forif.univ_hanyang.apply.dto.response.UnpaidUserResponse;
import forif.univ_hanyang.apply.entity.Apply;
import forif.univ_hanyang.apply.service.ApplyService;
import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Tag(name = "지원서", description = "지원서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/applies")
public class ApplyController {
    private final UserService userService;
    private final ApplyService applyService;

    @RequireJWT
    @PostMapping
    public ResponseEntity<Void> applyStudy(
            @RequestHeader("Authorization") String token,
            @RequestBody ApplyRequest request) {

        User user = userService.validateUserExist(token);
        applyService.applyStudy(request, user);

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param token 지원자의 토큰
     * @return <?> 형식으로 해서 null 일 때와 지원서가 있을 때 다른 객체를 반환하도록 함
     */
    @RequireJWT
    @GetMapping("/application")
    public ResponseEntity<?> getUserApplication(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        MyApplicationResponse application = applyService.getUserApplication(user);

        if (application == null) {
            ApplyResponse responseObj = new ApplyResponse(500, "지원서가 없습니다.");
            return new ResponseEntity<>(responseObj, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @RequireJWT
    @PatchMapping("/application")
    private ResponseEntity<Apply> patchApplication(
            @RequestHeader("Authorization") String token,
            @RequestBody ApplyRequest request
    ) throws InvocationTargetException, IllegalAccessException {
        User user = userService.validateUserExist(token);

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(applyService.patchApplication(user, request), HttpStatus.OK);
    }

    @RequireJWT
    @DeleteMapping("/application")
    private ResponseEntity<Void> deleteApplication(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);

        applyService.deleteApplication(user);

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @RequireJWT
    @PostMapping("/accept")
    public ResponseEntity<Void> acceptApplication(
            @RequestBody AcceptRequest request) {
        applyService.acceptApplication(request);

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping("/payment")
    public ResponseEntity<List<UnpaidUserResponse>> getUnpaidUsers(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        if (user.getAuthLv() == 1)
            throw new IllegalArgumentException("권한이 없습니다.");

        return new ResponseEntity<>(applyService.getUnpaidUsers(), HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping("/all")
    public ResponseEntity<?> getAllApplicationsOfStudy(
            @RequestParam Integer studyId,
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        Map<String, List<RankedStudyResponse>> applications = applyService.getAllApplicationsOfStudy(studyId, user);
        if (applications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @RequireJWT
    @GetMapping("/mentor/all")
    public ResponseEntity<?> getAllApplicationsOfStudyForMentor(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        Map<String, List<RankedStudyResponse>> applications = applyService.getAllApplicationsOfStudyForMentor(user);
        if (applications.isEmpty()) {
            ApplyResponse responseObj = new ApplyResponse(500, "지원서가 없습니다.");
            return new ResponseEntity<>(responseObj, HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }


    @RequireJWT
    @PatchMapping("/paid")
    public ResponseEntity<Void> patchIsPaid(
            @RequestHeader("Authorization") String token,
            @RequestBody IsPaidRequest request
    ) {
        User user = userService.validateUserExist(token);

        applyService.patchIsPaid(user, request);

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequireJWT
    @DeleteMapping("/allApplications")
    public ResponseEntity<Void> deleteAllApplications(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);

        applyService.deleteAllApplications(user);

        // 성공 시 200 OK 상태 코드와 함께 JSON 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
