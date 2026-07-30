package africa.civicbridge.api.entity;

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
    private Long id;

    private String initiative;

    private String status;

    private Integer progress;

    @Column(columnDefinition = "TEXT")
    private String note;

    public TrackerItem(String initiative, String status, Integer progress, String note) {
        this.initiative = initiative;
        this.status = status;
        this.progress = progress;
        this.note = note;
    }
}
