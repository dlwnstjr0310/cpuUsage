package org.practice.test.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.practice.test.domain.common.Status;
import org.practice.test.domain.entity.CpuUsage;
import org.practice.test.domain.entity.CpuUsageSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class CpuUsageRepositoryTest {

	@Autowired
	private CpuUsageRepository cpuUsageRepository;

	@Autowired
	private CpuUsageSummaryRepository cpuUsageSummaryRepository;

	@BeforeEach
	void setUp() {
		CpuUsage firstCreated = CpuUsage.builder()
				.usage(0.00972277045896508)
				.build();

		CpuUsage secondCreated = CpuUsage.builder()
				.usage(0.015638626330169103)
				.build();

		CpuUsage thirdCreated = CpuUsage.builder()
				.usage(0.02155448220137313)
				.build();

		CpuUsage fourthCreated = CpuUsage.builder()
				.usage(0.027470338072577155)
				.build();

		CpuUsage fifthCreated = CpuUsage.builder()
				.usage(0.03338619394378118)
				.build();

		firstCreated.setCreatedAt(LocalDateTime.now().minusMinutes(14));
		secondCreated.setCreatedAt(LocalDateTime.now().minusMinutes(13));
		thirdCreated.setCreatedAt(LocalDateTime.now().minusMinutes(12));
		fourthCreated.setCreatedAt(LocalDateTime.now().minusMinutes(11));
		fifthCreated.setCreatedAt(LocalDateTime.now().minusMinutes(10));

		cpuUsageRepository.saveAll(
				List.of(firstCreated, secondCreated, thirdCreated, fourthCreated, fifthCreated)
		);
	}

	@Test
	@DisplayName("분 단위 데이터 저장 후 조회 테스트")
	void saveCpuUsageSuccessTest() {
		// given
		CpuUsage cpuUsage = CpuUsage.builder()
				.usage(0.0393020498149852)
				.build();

		// when
		cpuUsageRepository.save(cpuUsage);

		// then
		List<CpuUsage> cpuUsageList = cpuUsageRepository.findAll();
		CpuUsage savedCpuUsage = cpuUsageList.get(cpuUsageList.size() - 1);

		assertEquals(6, cpuUsageList.size());
		assertEquals(0.0393020498149852, savedCpuUsage.getUsage());
	}

	@Test
	@DisplayName("사용량 요약 데이터 저장 후 조회 테스트")
	void cpuUsageSummaryTest() {
		// given
		double maxUsage = 0.03338619394378118;
		double avgUsage = (0.00972277045896508 + 0.015638626330169103 + 0.02155448220137313 + 0.027470338072577155 + 0.03338619394378118) / 5;
		double minUsage = 0.00972277045896508;

		List<CpuUsage> cpuUsageList = cpuUsageRepository.findAll();

		DoubleSummaryStatistics stats = cpuUsageList.stream()
				.mapToDouble(CpuUsage::getUsage)
				.summaryStatistics();

		CpuUsageSummary cpuUsageSummary = CpuUsageSummary.builder()
				.maxUsage(stats.getMax())
				.avgUsage(stats.getAverage())
				.minUsage(stats.getMin())
				.type(Status.Type.HOURS)
				.build();

		// when
		cpuUsageSummaryRepository.save(cpuUsageSummary);

		// then
		List<CpuUsageSummary> cpuUsageSummaryList = cpuUsageSummaryRepository.findAll();
		CpuUsageSummary savedCpuUsageSummary = cpuUsageSummaryList.get(cpuUsageSummaryList.size() - 1);

		assertEquals(1, cpuUsageSummaryList.size());
		assertEquals(maxUsage, savedCpuUsageSummary.getMaxUsage());
		assertEquals(avgUsage, savedCpuUsageSummary.getAvgUsage(), 0.0000000000001);
		assertEquals(minUsage, savedCpuUsageSummary.getMinUsage());
	}

}
