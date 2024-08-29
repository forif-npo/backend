package forif.univ_hanyang.apply.controller;

import forif.univ_hanyang.apply.dto.request.AcceptRequest;
import forif.univ_hanyang.apply.dto.request.ApplyRequest;
import forif.univ_hanyang.apply.dto.request.IsPaidRequest;
import forif.univ_hanyang.apply.dto.response.ApplyInfoResponse;
import forif.univ_hanyang.apply.dto.response.ApplyResponse;
import forif.univ_hanyang.apply.dto.response.MyApplicationResponse;
import forif.univ_hanyang.apply.dto.response.UserPaymentStatusResponse;
import forif.univ_hanyang.apply.entity.Apply;
import forif.univ_hanyang.apply.service.ApplyService;
import forif.univ_hanyang.user.entity.User;
import forif.univ_hanyang.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Tag(name = "지원서", description = "지원서 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplyController {
    private final UserService userService;
    private final ApplyService applyService;

    @GetMapping
    public ResponseEntity<List<ApplyInfoResponse>> getAllApplications(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        return new ResponseEntity<>(applyService.getAllApplications(user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> applyStudy(
            @RequestHeader("Authorization") String token,
            @RequestBody ApplyRequest request) {

        User user = userService.validateUserExist(token);
        applyService.applyStudy(request, user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param token 지원자의 토큰
     * @return <?> 형식으로 해서 null일 때와 지원서가 있을 때 다른 객체를 반환하도록 함
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserApplication(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        MyApplicationResponse application = applyService.getUserApplication(user);

        if (application == null) {
            ApplyResponse responseObj = new ApplyResponse(404, "지원서가 없습니다.");
            return new ResponseEntity<>(responseObj, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(application, HttpStatus.OK);
    }


    @PatchMapping("/me")
    private ResponseEntity<Apply> patchApplication(
            @RequestHeader("Authorization") String token,
            @RequestBody ApplyRequest request
    ) throws InvocationTargetException, IllegalAccessException {
        User user = userService.validateUserExist(token);

        return new ResponseEntity<>(applyService.patchApplication(user, request), HttpStatus.OK);
    }


    @DeleteMapping("/me")
    private ResponseEntity<Void> deleteApplication(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);

        applyService.deleteApplication(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/accept")
    public ResponseEntity<Void> acceptApplications(
            @RequestBody AcceptRequest request,
            @RequestHeader("Authorization") String token){
        User mentor = userService.validateUserExist(token);
        applyService.acceptApplications(mentor, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/unpaid-users")
    public ResponseEntity<List<UserPaymentStatusResponse>> getUnpaidUsers(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        if (user.getAuthLv() < 3)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");

        return new ResponseEntity<>(applyService.getUnpaidUsers(), HttpStatus.OK);
    }

    @GetMapping("/paid-users")
    public ResponseEntity<List<UserPaymentStatusResponse>> getPaidUsers(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        if (user.getAuthLv() == 1)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");

        return new ResponseEntity<>(applyService.getPaidUsers(), HttpStatus.OK);
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<?> getAllApplicationsOfStudy(
            @PathVariable Integer studyId,
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);
        Map<String, List<ApplyInfoResponse>> applications = applyService.getAllApplicationsOfStudy(studyId, user);
        if (applications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @PatchMapping("/payment-status")
    public ResponseEntity<Void> patchIsPaid(
            @RequestHeader("Authorization") String token,
            @RequestBody IsPaidRequest request
    ) {
        User user = userService.validateUserExist(token);

        applyService.patchIsPaid(user, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteAllApplications(
            @RequestHeader("Authorization") String token
    ) {
        User user = userService.validateUserExist(token);

        applyService.deleteAllApplications(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
