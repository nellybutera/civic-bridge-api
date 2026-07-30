package africa.civicbridge.api.entity;

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
    private Long id;

    private String authorName;

    private String authorRole;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    private Instant createdAt;

    public ForumPost(String authorName, String authorRole, String title, String body) {
        this.authorName = authorName;
        this.authorRole = authorRole;
        this.title = title;
        this.body = body;
        this.createdAt = Instant.now();
    }
}
