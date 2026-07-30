package africa.civicbridge.api.controller;

import africa.civicbridge.api.entity.TrackerItem;
import africa.civicbridge.api.repository.TrackerItemRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracker")
@Tag(name = "Regional Tracker", description = "AU/EAC initiative progress tracker. Reads are public; writes require an Admin JWT.")
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
    @SecurityRequirement(name = "bearerAuth")
    public TrackerItem create(@RequestBody TrackerItem item) {
        item.setId(null);
        return repo.save(item);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
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
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
