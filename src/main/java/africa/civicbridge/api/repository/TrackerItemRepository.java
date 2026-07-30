package africa.civicbridge.api.repository;

import africa.civicbridge.api.entity.TrackerItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackerItemRepository extends JpaRepository<TrackerItem, Long> {
}
