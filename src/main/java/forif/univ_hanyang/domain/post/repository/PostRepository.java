package forif.univ_hanyang.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import forif.univ_hanyang.domain.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<List<Post>> findAllByType(String type);
    @Query("SELECT COALESCE(MAX(p.id), 0) FROM Post p")
    Optional<Integer> findMaxId();
}
