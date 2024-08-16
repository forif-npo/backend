package forif.univ_hanyang.post.controller;

import forif.univ_hanyang.jwt.RequireJWT;
import forif.univ_hanyang.post.dto.request.AnnouncementRequest;
import forif.univ_hanyang.post.dto.response.AnnouncementResponse;
import forif.univ_hanyang.post.dto.response.FAQResponse;
import forif.univ_hanyang.post.dto.response.TechResponse;
import forif.univ_hanyang.post.service.PostService;
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
            @RequestBody AnnouncementRequest announcementRequest
    ) {
        postService.createAnnouncement(announcementRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/faqs")
    public ResponseEntity<List<FAQResponse>> getFAQs() {
        return new ResponseEntity<>(postService.getFAQs(), HttpStatus.OK);
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
