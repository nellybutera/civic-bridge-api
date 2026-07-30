package africa.civicbridge.api.controller;

import africa.civicbridge.api.entity.TrackerItem;
import africa.civicbridge.api.repository.TrackerItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracker")
public class TrackerController {

    private final TrackerItemRepository repo;

    public TrackerController(TrackerItemRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<TrackerItem> all() {
        return repo.findAll();
    }

    @PostMapping
    public TrackerItem create(@RequestBody TrackerItem item) {
        item.setId(null);
        return repo.save(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackerItem> update(@PathVariable Long id, @RequestBody TrackerItem update) {
        return repo.findById(id).map(existing -> {
            existing.setInitiative(update.getInitiative());
            existing.setStatus(update.getStatus());
            existing.setProgress(update.getProgress());
            existing.setNote(update.getNote());
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
