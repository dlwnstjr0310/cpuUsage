package org.practice.test.service;

import lombok.RequiredArgsConstructor;
import org.practice.test.domain.common.Status;
import org.practice.test.domain.entity.CpuUsage;
import org.practice.test.domain.entity.CpuUsageSummary;
import org.practice.test.exception.cpuUsage.OutOfBoundaryException;
import org.practice.test.model.response.UsageResponse;
import org.practice.test.repository.CpuUsageRepository;
import org.practice.test.repository.CpuUsageSummaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CpuUsageService {

	private final CpuUsageRepository cpuUsageRepository;
	private final CpuUsageSummaryRepository cpuUsageSummaryRepository;

	@Transactional(readOnly = true)
	public List<UsageResponse.Base> getMinuteCpuUsage(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		if (startDateTime.isBefore(LocalDateTime.now().minusDays(7))) {
			throw new OutOfBoundaryException();
		}

		List<CpuUsage> cpuUsageList = cpuUsageRepository.findByCreatedAtBetween(startDateTime, endDateTime);

		return UsageResponse.Base.of(cpuUsageList);
	}

	@Transactional(readOnly = true)
	public List<UsageResponse.Target> getHourlyCpuUsage(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		if (startDateTime.isBefore(LocalDateTime.now().minusMonths(3))) {
			throw new OutOfBoundaryException();
		}

		List<CpuUsageSummary> summaryList = cpuUsageSummaryRepository.findByTypeAndCreatedAtBetween(
				Status.Type.HOURS,
				startDateTime,
				endDateTime
		);

		return UsageResponse.Target.of(summaryList);
	}

	@Transactional(readOnly = true)
	public List<UsageResponse.Target> getDailyCpuUsage(LocalDate startDate, LocalDate endDate) {

		LocalDateTime startDateTime = startDate.atStartOfDay();

		if (startDateTime.isBefore(LocalDateTime.now().minusYears(1))) {
			throw new OutOfBoundaryException();
		}

		List<CpuUsageSummary> summaryList = cpuUsageSummaryRepository.findByTypeAndCreatedAtBetween(
				Status.Type.DAYS,
				startDateTime,
				endDate.atStartOfDay()
		);

		return UsageResponse.Target.of(summaryList);
	}
}
