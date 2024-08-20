package forif.univ_hanyang.post.service;

import forif.univ_hanyang.post.dto.request.FAQRequest;
import forif.univ_hanyang.post.entity.Post;
import forif.univ_hanyang.post.dto.request.AnnouncementRequest;
import forif.univ_hanyang.post.dto.response.AnnouncementResponse;
import forif.univ_hanyang.post.dto.response.FAQResponse;
import forif.univ_hanyang.post.dto.response.TechResponse;
import forif.univ_hanyang.post.entity.PostFAQ;
import forif.univ_hanyang.post.repository.PostFAQRepository;
import forif.univ_hanyang.post.repository.PostRepository;
import forif.univ_hanyang.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostFAQRepository postFAQRepository;

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

    @Transactional
    public void createAnnouncement(User user, AnnouncementRequest announcementRequest) {
        if(user.getAuthLv()<3){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        Post post = new Post();
        post.setId(postRepository.findMaxId().orElse(0) + 1);
        post.setType("공지사항");
        post.setCreatedBy(announcementRequest.getCreatedBy());
        post.setCreatedAt(LocalDateTime.now().toString());
        post.setTitle(announcementRequest.getTitle());
        post.setContent(announcementRequest.getContent());
        postRepository.save(post);
    }

    @Transactional
    public void updateAnnouncement(User user, Integer id, AnnouncementRequest announcementRequest) {
        if(user.getAuthLv()<3){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 공지사항이 없습니다."));
        post.setTitle(announcementRequest.getTitle());
        post.setContent(announcementRequest.getContent());
        postRepository.save(post);
    }

    @Transactional
    public void deleteAnnouncement(User user, Integer id) {
        if(user.getAuthLv()<3){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 공지사항이 없습니다."));
        postRepository.delete(post);
    }

    public List<FAQResponse> getFAQs() {
        List<Post> postList = postRepository.findAllByType("FAQ")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FAQ가 없습니다."));
        return FAQResponse.from(postList);
    }

    @Transactional
    public void createFAQ(User user, FAQRequest request) {
        if(user.getAuthLv()<3){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        Integer newId = postRepository.findMaxId().orElse(0) + 1;
        Post post = new Post();
        post.setId(newId);
        post.setType("FAQ");
        post.setCreatedAt(LocalDateTime.now().toString());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        PostFAQ postFAQ = new PostFAQ();
        postFAQ.setPost(post);
        postFAQ.setTag(request.getTag());
        postFAQ.setId(newId);
        post.setPostFAQ(postFAQ);

        postRepository.save(post);
        postFAQRepository.save(postFAQ);
    }

    @Transactional
    public void updateFAQ(User user, Integer id, FAQRequest request) {
        if(user.getAuthLv()<3){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        PostFAQ postFAQ = postFAQRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 FAQ가 없습니다."));
        postFAQ.setTag(request.getTag());
        postFAQ.getPost().setTitle(request.getTitle());
        postFAQ.getPost().setContent(request.getContent());

        postFAQRepository.save(postFAQ);
    }

    @Transactional
    public void deleteFAQ(User user, Integer id) {
        if(user.getAuthLv()<3){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
        PostFAQ postFAQ = postFAQRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 FAQ가 없습니다."));
        postFAQRepository.delete(postFAQ);
        postRepository.delete(postFAQ.getPost());
    }

    public List<TechResponse> getTechs() {
        List<Post> postList = postRepository.findAllByType("기술 블로그")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "기술 블로그가 없습니다."));
        return TechResponse.from(postList);
    }

    public TechResponse getTech(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 기술 블로그 글이 없습니다."));
        return TechResponse.from(post);
    }
}
