package africa.civicbridge.api.controller;

import africa.civicbridge.api.entity.ForumPost;
import africa.civicbridge.api.repository.ForumPostRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
@Tag(name = "Forum", description = "Discussion forum. Reads are public; posting requires any logged-in user's JWT; deleting requires Moderator/Admin.")
public class ForumController {

    private final ForumPostRepository repo;

    public ForumController(ForumPostRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<ForumPost> all() {
        return repo.findAll();
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ForumPost create(@RequestBody ForumPost post) {
        post.setId(null);
        return repo.save(post);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
