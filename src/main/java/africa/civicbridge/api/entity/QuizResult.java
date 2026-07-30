package africa.civicbridge.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "quiz_results")
@Getter
@Setter
@NoArgsConstructor
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quizId;

    private Long userId;

    private Integer scorePercent;

    private Instant completedAt;

    public QuizResult(Long quizId, Long userId, Integer scorePercent) {
        this.quizId = quizId;
        this.userId = userId;
        this.scorePercent = scorePercent;
        this.completedAt = Instant.now();
    }
}
