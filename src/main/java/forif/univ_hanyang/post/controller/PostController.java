package forif.univ_hanyang.post.controller;

import forif.univ_hanyang.post.dto.AnnouncementResponse;
import forif.univ_hanyang.post.dto.FAQResponse;
import forif.univ_hanyang.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/faqs")
    public ResponseEntity<List<FAQResponse>> getFAQs() {
        return new ResponseEntity<>(postService.getFAQs(), HttpStatus.OK);
    }

}
