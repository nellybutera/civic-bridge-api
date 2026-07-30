package africa.civicbridge.api.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tracker_items")
@Getter
@Setter
@NoArgsConstructor
public class TrackerItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, example = "5")
    private Long id;

    @Schema(example = "EAC Single Currency Protocol")
    private String initiative;

    @Schema(example = "Early Stage")
    private String status;

    @Schema(example = "20")
    private Integer progress;

    @Column(columnDefinition = "TEXT")
    @Schema(example = "Roadmap adopted in principle; convergence criteria for partner-state economies still under negotiation.")
    private String note;

    public TrackerItem(String initiative, String status, Integer progress, String note) {
        this.initiative = initiative;
        this.status = status;
        this.progress = progress;
        this.note = note;
    }
}
