package africa.civicbridge.api.repository;

import africa.civicbridge.api.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
