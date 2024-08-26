package forif.univ_hanyang.post.controller;

import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.post.dto.request.AnnouncementRequest;
import forif.univ_hanyang.post.dto.request.AnnouncementUpdateRequest;
import forif.univ_hanyang.post.dto.request.FAQRequest;
import forif.univ_hanyang.post.dto.response.AnnouncementResponse;
import forif.univ_hanyang.post.dto.response.FAQResponse;
import forif.univ_hanyang.post.dto.response.TechResponse;
import forif.univ_hanyang.post.service.PostService;
import forif.univ_hanyang.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/announcements")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncements() {
        return new ResponseEntity<>(postService.getAnnouncements(), HttpStatus.OK);
    }

    @GetMapping("/announcements/{id}")
    public ResponseEntity<AnnouncementResponse> getAnnouncement(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(postService.getAnnouncement(id), HttpStatus.OK);
    }

    @RequireJWT
    @PostMapping("/announcements")
    public ResponseEntity<Void> createAnnouncement(
            @RequestBody AnnouncementRequest announcementRequest,
            @RequestHeader("Authorization") String token
    ) {
        postService.createAnnouncement(userService.validateUserExist(token), announcementRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequireJWT
    @PatchMapping("/announcements/{id}")
    public ResponseEntity<Void> updateAnnouncement(
            @PathVariable Integer id,
            @RequestBody AnnouncementUpdateRequest request,
            @RequestHeader("Authorization") String token
    ) {
        postService.updateAnnouncement(userService.validateUserExist(token), id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequireJWT
    @DeleteMapping("/announcements/{id}")
    public ResponseEntity<Void> deleteAnnouncement(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ) {
        postService.deleteAnnouncement(userService.validateUserExist(token), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/faqs")
    public ResponseEntity<List<FAQResponse>> getFAQs() {
        return new ResponseEntity<>(postService.getFAQs(), HttpStatus.OK);
    }

    @RequireJWT
    @PostMapping("/faqs")
    public ResponseEntity<Void> createFAQ(
            @RequestBody FAQRequest request,
            @RequestHeader("Authorization") String token
            ) {
        postService.createFAQ(userService.validateUserExist(token), request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequireJWT
    @PatchMapping("/faqs/{id}")
    public ResponseEntity<Void> updateFAQ(
            @PathVariable Integer id,
            @RequestBody FAQRequest request,
            @RequestHeader("Authorization") String token
    ) {
        postService.updateFAQ(userService.validateUserExist(token), id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequireJWT
    @DeleteMapping("/faqs/{id}")
    public ResponseEntity<Void> deleteFAQ(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ) {
        postService.deleteFAQ(userService.validateUserExist(token), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/techs")
    public ResponseEntity<List<TechResponse>> getTechs() {
        return new ResponseEntity<>(postService.getTechs(), HttpStatus.OK);
    }

    @GetMapping("/techs/{id}")
    public ResponseEntity<TechResponse> getTech(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(postService.getTech(id), HttpStatus.OK);
    }

}
