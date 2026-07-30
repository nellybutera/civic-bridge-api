package africa.civicbridge.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "forum_posts")
@Getter
@Setter
@NoArgsConstructor
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "2")
    private Long id;

    @Schema(example = "Nia Uwimana")
    private String authorName;

    @Schema(example = "Youth User")
    private String authorRole;

    @Schema(example = "Does anyone track county-level budget hearings?")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Schema(example = "I want to attend one in person but can't find a public schedule anywhere. Any tips?")
    private String body;

    @Schema(example = "Regional Trade", description = "The discussion room this post belongs to. Null/omitted posts appear under General.")
    private String topic;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "2026-07-22T14:30:00Z")
    private Instant createdAt;

    public ForumPost(String authorName, String authorRole, String title, String body) {
        this.authorName = authorName;
        this.authorRole = authorRole;
        this.title = title;
        this.body = body;
        this.createdAt = Instant.now();
    }

    public ForumPost(String authorName, String authorRole, String title, String body, String topic) {
        this.authorName = authorName;
        this.authorRole = authorRole;
        this.title = title;
        this.body = body;
        this.topic = topic;
        this.createdAt = Instant.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
