package org.practice.test.repository;

import org.practice.test.domain.common.Status;
import org.practice.test.domain.entity.CpuUsageSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CpuUsageSummaryRepository extends JpaRepository<CpuUsageSummary, Long> {

	List<CpuUsageSummary> findByTypeAndCreatedAtBetween(Status.Type type, LocalDateTime startDateTime, LocalDateTime endDateTime);

	void deleteByTypeIsAndCreatedAtBefore(Status.Type type, LocalDateTime createdAt);
}
