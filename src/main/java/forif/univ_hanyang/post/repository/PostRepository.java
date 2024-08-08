package forif.univ_hanyang.post.repository;

import forif.univ_hanyang.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    Optional<List<Post>> findAllByType(String type);
}
