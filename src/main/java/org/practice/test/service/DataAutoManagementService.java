package org.practice.test.service;

import com.sun.management.OperatingSystemMXBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.practice.test.domain.common.Status;
import org.practice.test.domain.entity.CpuUsage;
import org.practice.test.domain.entity.CpuUsageSummary;
import org.practice.test.exception.dataManagement.InvalidCpuUsageListSizeException;
import org.practice.test.exception.dataManagement.UnexpectedDataException;
import org.practice.test.repository.CpuUsageRepository;
import org.practice.test.repository.CpuUsageSummaryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.ManagementFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataAutoManagementService {

	private final CpuUsageRepository cpuUsageRepository;
	private final CpuUsageSummaryRepository cpuUsageSummaryRepository;

	@Transactional
	@Scheduled(cron = "0 * * * * *")
	public void autoCreateCpuUsage() {

		OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

		double cpuLoad = osBean.getCpuLoad();

		try {
			cpuUsageRepository.save(
					CpuUsage.builder()
							.usage(cpuLoad)
							.build()
			);
		} catch (Exception e) {
			log.error("CPU 사용량 저장중 예기치 않은 오류가 발생했습니다.", e);
			throw new UnexpectedDataException();
		}
	}

	@Transactional
	@Scheduled(cron = "0 0 * * * *")
	public void autoCreateHourlyCpuUsage() {

		List<CpuUsage> usageList = cpuUsageRepository.findByCreatedAtBetween(
				LocalDateTime.now().minusHours(1).withMinute(0).withSecond(0),
				LocalDateTime.now().withMinute(0).withSecond(0)
		);

		if (usageList.size() != 60) {
			throw new InvalidCpuUsageListSizeException();
		}

		DoubleSummaryStatistics stats = usageList.stream()
				.mapToDouble(CpuUsage::getUsage)
				.summaryStatistics();

		cpuUsageSummaryRepository.save(
				CpuUsageSummary.builder()
						.maxUsage(stats.getMax())
						.avgUsage(stats.getAverage())
						.minUsage(stats.getMin())
						.type(Status.Type.HOURS)
						.build()
		);
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void autoCreateDailyCpuUsage() {

		List<CpuUsage> usageList = cpuUsageRepository.findByCreatedAtBetween(
				LocalDate.now().minusDays(1).atStartOfDay(),
				LocalDate.now().minusDays(1).atTime(23, 59, 59)
		);

		if (usageList.size() != 60) {
			throw new InvalidCpuUsageListSizeException();
		}

		DoubleSummaryStatistics stats = usageList.stream()
				.mapToDouble(CpuUsage::getUsage)
				.summaryStatistics();

		cpuUsageSummaryRepository.save(
				CpuUsageSummary.builder()
						.maxUsage(stats.getMax())
						.avgUsage(stats.getAverage())
						.minUsage(stats.getMin())
						.type(Status.Type.DAYS)
						.build()
		);
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	public void autoDeleteExpiredData() {

		cpuUsageRepository.deleteByCreatedAtBefore(LocalDate.now().minusDays(7).atStartOfDay());
		cpuUsageSummaryRepository.deleteByTypeIsAndCreatedAtBefore(Status.Type.HOURS, LocalDate.now().minusMonths(3).atStartOfDay());
		cpuUsageSummaryRepository.deleteByTypeIsAndCreatedAtBefore(Status.Type.DAYS, LocalDate.now().minusYears(1).atStartOfDay());
	}
}
