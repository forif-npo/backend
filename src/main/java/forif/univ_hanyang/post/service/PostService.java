package forif.univ_hanyang.post.service;

import forif.univ_hanyang.post.domain.Post;
import forif.univ_hanyang.post.dto.AnnouncementResponse;
import forif.univ_hanyang.post.dto.FAQResponse;
import forif.univ_hanyang.post.dto.HackathonResponse;
import forif.univ_hanyang.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<AnnouncementResponse> getAnnouncements() {
        List<Post> postList = postRepository.findAllByType("공지사항")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "공지사항이 없습니다."));
        return AnnouncementResponse.from(postList);
    }

    public AnnouncementResponse getAnnouncement(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 공지사항이 없습니다."));
        return AnnouncementResponse.from(post);
    }

    public List<FAQResponse> getFAQs() {
        List<Post> postList = postRepository.findAllByType("FAQ")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FAQ가 없습니다."));
        return FAQResponse.from(postList);
    }
}
