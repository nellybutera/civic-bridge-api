package africa.civicbridge.api.repository;

import africa.civicbridge.api.entity.CivicContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CivicContentRepository extends JpaRepository<CivicContent, Long> {
}
