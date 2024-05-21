package org.practice.test.repository;

import org.practice.test.domain.entity.CpuUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CpuUsageRepository extends JpaRepository<CpuUsage, Long> {

	List<CpuUsage> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

	void deleteByCreatedAtBefore(LocalDateTime DateTime);

}
