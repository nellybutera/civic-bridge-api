package africa.civicbridge.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "5")
    private Long id;

    @Schema(example = "Understanding the AfCFTA Guided Trade Initiative")
    private String title;

    @Schema(example = "Regional Integration")
    private String category;

    @Column(length = 1000)
    @Schema(example = "A quick primer on which goods are trading tariff-free under AfCFTA today, and which are still pending.")
    private String summary;

    @Column(columnDefinition = "TEXT")
    @Schema(example = "The Guided Trade Initiative lets a small group of pilot countries begin trading under AfCFTA rules ahead of full continental rollout, so early data can be used to fix implementation gaps before scaling up.")
    private String body;

    @Schema(example = "4")
    private Integer readMinutes;

    public CivicContent(String title, String category, String summary, String body, Integer readMinutes) {
        this.title = title;
        this.category = category;
        this.summary = summary;
        this.body = body;
        this.readMinutes = readMinutes;
    }
}
