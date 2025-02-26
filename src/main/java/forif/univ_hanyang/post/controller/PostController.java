package forif.univ_hanyang.post.controller;

import forif.univ_hanyang.post.dto.request.AnnouncementRequest;
import forif.univ_hanyang.post.dto.request.AnnouncementUpdateRequest;
import forif.univ_hanyang.post.dto.request.FAQRequest;
import forif.univ_hanyang.post.dto.request.TechRequest;
import forif.univ_hanyang.post.dto.response.AnnouncementResponse;
import forif.univ_hanyang.post.dto.response.FAQResponse;
import forif.univ_hanyang.post.dto.response.TechResponse;
import forif.univ_hanyang.post.service.PostService;
import forif.univ_hanyang.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시글", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @Operation(
            summary = "공지사항 조회",
            description = "전체 공지사항을 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @GetMapping("/announcements")
    public ResponseEntity<List<AnnouncementResponse>> getAnnouncements() {
        return new ResponseEntity<>(postService.getAnnouncements(), HttpStatus.OK);
    }

    @Operation(
            summary = "공지사항 조회",
            description = "해당 공지사항의 정보를 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @GetMapping("/announcements/{id}")
    public ResponseEntity<AnnouncementResponse> getAnnouncement(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(postService.getAnnouncement(id), HttpStatus.OK);
    }

    @Operation(
            summary = "공지사항 생성",
            description = "공지사항을 생성함",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @PostMapping("/announcements")
    public ResponseEntity<Void> createAnnouncement(
            @RequestBody AnnouncementRequest announcementRequest,
            @RequestHeader("Authorization") String token
    ) {
        postService.createAnnouncement(userService.validateUserExist(token), announcementRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "공지사항 수정",
            description = "해당 공지사항을 수정함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @PatchMapping("/announcements/{id}")
    public ResponseEntity<Void> updateAnnouncement(
            @PathVariable Integer id,
            @RequestBody AnnouncementUpdateRequest request,
            @RequestHeader("Authorization") String token
    ) {
        postService.updateAnnouncement(userService.validateUserExist(token), id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "공지사항 삭제",
            description = "해당 공지사항을 삭제함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @DeleteMapping("/announcements/{id}")
    public ResponseEntity<Void> deleteAnnouncement(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ) {
        postService.deleteAnnouncement(userService.validateUserExist(token), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "FAQ 조회",
            description = "전체 FAQ를 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @GetMapping("/faqs")
    public ResponseEntity<List<FAQResponse>> getFAQs() {
        return new ResponseEntity<>(postService.getFAQs(), HttpStatus.OK);
    }

    @Operation(
            summary = "FAQ 생성",
            description = "FAQ를 생성함",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @PostMapping("/faqs")
    public ResponseEntity<Void> createFAQ(
            @RequestBody FAQRequest request,
            @RequestHeader("Authorization") String token
            ) {
        postService.createFAQ(userService.validateUserExist(token), request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "FAQ 수정",
            description = "해당 FAQ를 수정함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @PatchMapping("/faqs/{id}")
    public ResponseEntity<Void> updateFAQ(
            @PathVariable Integer id,
            @RequestBody FAQRequest request,
            @RequestHeader("Authorization") String token
    ) {
        postService.updateFAQ(userService.validateUserExist(token), id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "FAQ 삭제",
            description = "해당 FAQ를 삭제함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @DeleteMapping("/faqs/{id}")
    public ResponseEntity<Void> deleteFAQ(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ) {
        postService.deleteFAQ(userService.validateUserExist(token), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "기술 블로그 조회",
            description = "전체 기술 블로그를 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @GetMapping("/techs")
    public ResponseEntity<List<TechResponse>> getTechs() {
        return new ResponseEntity<>(postService.getTechs(), HttpStatus.OK);
    }

    @Operation(
            summary = "기술 블로그 조회",
            description = "해당 기술 블로그의 정보를 조회함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "404", description = "요청 데이터에 대한 정보가 없음")
            }
    )
    @GetMapping("/techs/{id}")
    public ResponseEntity<TechResponse> getTech(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(postService.getTech(id), HttpStatus.OK);
    }

    @Operation(
            summary = "기술 블로그 글 작성",
            description = "기술 블로그 글을 작성함",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @PostMapping("/techs")
    public ResponseEntity<TechResponse> createTech(
            @RequestBody TechRequest request,
            @RequestHeader("Authorization") String token
    ) {
        TechResponse response = postService.createTech(userService.validateUserExist(token), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "기술 블로그 수정",
            description = "해당 기술 블로그를 수정함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @PatchMapping("/techs/{id}")
    public ResponseEntity<TechResponse> updateTech(
            @PathVariable Integer id,
            @RequestBody TechRequest request,
            @RequestHeader("Authorization") String token
    ) {
        TechResponse response = postService.updateTech(userService.validateUserExist(token), id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(
            summary = "기술 블로그 삭제",
            description = "해당 기술 블로그를 삭제함",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "403", description = "권한이 없음")
            }
    )
    @DeleteMapping("/techs/{id}")
    public ResponseEntity<Void> deleteTech(
            @PathVariable Integer id,
            @RequestHeader("Authorization") String token
    ) {
        postService.deleteTech(userService.validateUserExist(token), id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
