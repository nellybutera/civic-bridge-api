package africa.civicbridge.api.controller;

import africa.civicbridge.api.entity.CivicContent;
import africa.civicbridge.api.repository.CivicContentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final CivicContentRepository repo;

    public ContentController(CivicContentRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<CivicContent> all() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CivicContent> one(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CivicContent create(@RequestBody CivicContent content) {
        return repo.save(content);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CivicContent> update(@PathVariable Long id, @RequestBody CivicContent update) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(update.getTitle());
            existing.setCategory(update.getCategory());
            existing.setSummary(update.getSummary());
            existing.setBody(update.getBody());
            existing.setReadMinutes(update.getReadMinutes());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
