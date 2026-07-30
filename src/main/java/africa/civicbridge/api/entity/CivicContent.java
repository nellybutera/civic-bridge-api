package africa.civicbridge.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "civic_content")
@Getter
@Setter
@NoArgsConstructor
public class CivicContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String category;

    @Column(length = 1000)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String body;

    private Integer readMinutes;

    public CivicContent(String title, String category, String summary, String body, Integer readMinutes) {
        this.title = title;
        this.category = category;
        this.summary = summary;
        this.body = body;
        this.readMinutes = readMinutes;
    }
}
