package forif.univ_hanyang.domain.post.service;

import forif.univ_hanyang.domain.post.dto.request.AnnouncementRequest;
import forif.univ_hanyang.domain.post.dto.request.AnnouncementUpdateRequest;
import forif.univ_hanyang.domain.post.dto.request.FAQRequest;
import forif.univ_hanyang.domain.post.dto.request.TechRequest;
import forif.univ_hanyang.domain.post.dto.response.AnnouncementResponse;
import forif.univ_hanyang.domain.post.dto.response.FAQResponse;
import forif.univ_hanyang.domain.post.dto.response.TechResponse;
import forif.univ_hanyang.domain.post.entity.Post;
import forif.univ_hanyang.domain.post.repository.PostRepository;
import forif.univ_hanyang.domain.user.entity.User;
import forif.univ_hanyang.common.exception.ErrorCode;
import forif.univ_hanyang.common.exception.ForifException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public List<AnnouncementResponse> getAnnouncements() {
        List<Post> postList = postRepository.findAllByType("공지사항")
                .orElseThrow(() -> new ForifException(ErrorCode.NOTICE_NOT_FOUND));
        return AnnouncementResponse.from(postList);
    }

    @Transactional
    public AnnouncementResponse getAnnouncement(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ForifException(ErrorCode.SPECIFIC_NOTICE_NOT_FOUND));
        return AnnouncementResponse.from(post);
    }

    @Transactional
    public void createAnnouncement(User user, AnnouncementRequest announcementRequest) {
        if(user.getAuthLv()<3){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }
        Post post = new Post();
        post.setType("공지사항");
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now().toString());
        post.setTitle(announcementRequest.getTitle());
        post.setContent(announcementRequest.getContent());
        postRepository.save(post);
    }

    @Transactional
    public void updateAnnouncement(User user, Integer id, AnnouncementUpdateRequest request) {
        if(user.getAuthLv()<3){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ForifException(ErrorCode.SPECIFIC_NOTICE_NOT_FOUND));
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        postRepository.save(post);
    }

    @Transactional
    public void deleteAnnouncement(User user, Integer id) {
        if(user.getAuthLv()<3){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ForifException(ErrorCode.SPECIFIC_NOTICE_NOT_FOUND));
        postRepository.delete(post);
    }

    @Transactional
    public List<FAQResponse> getFAQs() {
        List<Post> postList = postRepository.findAllByType("FAQ")
                .orElseThrow(() -> new ForifException(ErrorCode.FAQ_NOT_FOUND));
        return FAQResponse.from(postList);
    }

    @Transactional
    public void createFAQ(User user, FAQRequest request) {
        if(user.getAuthLv()<3){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }
        Post post = new Post();
        post.setType("FAQ");
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now().toString());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        postRepository.save(post);
    }

    @Transactional
    public void updateFAQ(User user, Integer id, FAQRequest request) {
        if(user.getAuthLv()<3){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }
        Post postFAQ = postRepository.findById(id)
                .orElseThrow(() -> new ForifException(ErrorCode.SPECIFIC_FAQ_NOT_FOUND));

        postFAQ.setTitle(request.getTitle());
        postFAQ.setContent(request.getContent());

        postRepository.save(postFAQ);
    }

    @Transactional
    public void deleteFAQ(User user, Integer id) {
        if(user.getAuthLv()<3){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        Post postFAQ = postRepository.findById(id)
                .orElseThrow(() -> new ForifException(ErrorCode.SPECIFIC_FAQ_NOT_FOUND));

        postRepository.delete(postFAQ);
    }

    @Transactional
    public List<TechResponse> getTechs() {
        List<Post> postList = postRepository.findAllByType("기술 블로그")
                .orElseThrow(() -> new ForifException(ErrorCode.TECH_BLOG_NOT_FOUND));
        return TechResponse.from(postList);
    }

    @Transactional
    public TechResponse getTech(Integer id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ForifException(ErrorCode.SPECIFIC_TECH_BLOG_NOT_FOUND));
        return TechResponse.from(post);
    }

    @Transactional
    public TechResponse createTech(User user, TechRequest request) {
        if(user.getAuthLv() == 1){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }
        Post post = new Post();
        post.setType("기술 블로그");
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now().toString());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setTag(request.getTag());

        postRepository.save(post);
        return TechResponse.from(post);
    }

    @Transactional
    public TechResponse updateTech(User user, Integer id, TechRequest request) {
        if(user.getAuthLv() == 1){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }
        Post postTech = postRepository.findById(id)
                .orElseThrow(() -> new ForifException(ErrorCode.SPECIFIC_TECH_BLOG_NOT_FOUND));

        if(user.getAuthLv() < 3 && !postTech.getUser().getId().equals(user.getId())){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        postTech.setTitle(request.getTitle());
        postTech.setContent(request.getContent());
        postTech.setTag(request.getTag());

        postRepository.save(postTech);

        return TechResponse.from(postTech);
    }

    @Transactional
    public void deleteTech(User user, Integer id) {
        if(user.getAuthLv() == 1){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }

        Post postTech = postRepository.findById(id)
                .orElseThrow(() -> new ForifException(ErrorCode.SPECIFIC_TECH_BLOG_NOT_FOUND));

        if(user.getAuthLv() < 3 && !postTech.getUser().getId().equals(user.getId())){
            throw new ForifException(ErrorCode.INSUFFICIENT_PERMISSION);
        }


        postRepository.delete(postTech);
    }
}
