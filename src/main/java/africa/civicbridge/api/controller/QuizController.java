package africa.civicbridge.api.controller;

import africa.civicbridge.api.entity.Quiz;
import africa.civicbridge.api.entity.QuizResult;
import africa.civicbridge.api.repository.QuizRepository;
import africa.civicbridge.api.repository.QuizResultRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
@Tag(name = "Quizzes", description = "Quiz content and result submission. Reads are public; submitting a result requires any logged-in user's JWT.")
public class QuizController {

    private final QuizRepository quizzes;
    private final QuizResultRepository results;

    public QuizController(QuizRepository quizzes, QuizResultRepository results) {
        this.quizzes = quizzes;
        this.results = results;
    }

    @GetMapping
    public List<Quiz> all() {
        return quizzes.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> one(@PathVariable Long id) {
        return quizzes.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/results")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<QuizResult> submitResult(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        if (!quizzes.existsById(id)) return ResponseEntity.notFound().build();
        Long userId = Long.valueOf(String.valueOf(body.get("userId")));
        Integer scorePercent = Integer.valueOf(String.valueOf(body.get("scorePercent")));
        QuizResult saved = results.save(new QuizResult(id, userId, scorePercent));
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/results/{userId}")
    public List<QuizResult> resultsForUser(@PathVariable Long userId) {
        return results.findByUserId(userId);
    }
}
