package africa.civicbridge.api.repository;

import africa.civicbridge.api.entity.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
    boolean existsByTitle(String title);
}
