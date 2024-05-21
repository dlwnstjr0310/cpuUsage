package org.practice.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice.test.domain.common.Status;
import org.practice.test.domain.entity.CpuUsage;
import org.practice.test.domain.entity.CpuUsageSummary;
import org.practice.test.exception.cpuUsage.OutOfBoundaryException;
import org.practice.test.model.response.UsageResponse;
import org.practice.test.repository.CpuUsageRepository;
import org.practice.test.repository.CpuUsageSummaryRepository;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class CpuUsageServiceTest {

	@Mock
	private CpuUsageRepository cpuUsageRepository;

	@Mock
	private CpuUsageSummaryRepository cpuUsageSummaryRepository;

	@InjectMocks
	private CpuUsageService cpuUsageService;

	@Nested
	@DisplayName("분 단위 CPU 사용량 조회 테스트")
	class minuteCpuUsageTest {

		@Test
		@DisplayName("조회 성공 테스트")
		void getMinuteCpuUsageSuccessTest() {
			// given
			LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
			LocalDateTime endDateTime = LocalDateTime.now();

			CpuUsage cpuUsage = CpuUsage.builder()
					.usage(0.005)
					.build();

			cpuUsage.setCreatedAt(LocalDateTime.now());

			List<CpuUsage> cpuUsageList = Collections.singletonList(cpuUsage);

			// when
			Mockito.when(cpuUsageRepository.findByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
					.thenReturn(cpuUsageList);

			// then
			List<UsageResponse.Base> result = cpuUsageService.getMinuteCpuUsage(startDateTime, endDateTime);
			assertEquals(1, result.size());
			assertEquals("0.50%", result.get(0).getUsage());
		}

		@Test
		@DisplayName("조회 실패 테스트")
		void getMinuteCpuUsageFailTest() {
			// given
			LocalDateTime startDateTime = LocalDateTime.now().minusDays(8);
			LocalDateTime endDateTime = LocalDateTime.now();

			// when

			// then
			assertThrows(OutOfBoundaryException.class, () -> cpuUsageService.getMinuteCpuUsage(startDateTime, endDateTime));
		}

	}

	@Nested
	@DisplayName("시간 단위 CPU 사용량 조회 테스트")
	class hourlyCpuUsageTest {

		@Test
		@DisplayName("조회 성공 테스트")
		void getHourlyCpuUsageSuccessTest() {
			// given
			LocalDateTime startDateTime = LocalDateTime.now().minusMonths(1);
			LocalDateTime endDateTime = LocalDateTime.now();

			CpuUsageSummary cpuUsageSummary = CpuUsageSummary.builder()
					.type(Status.Type.HOURS)
					.maxUsage(0.04046215558992062)
					.avgUsage(0.018318524244537497)
					.minUsage(0.0)
					.build();

			cpuUsageSummary.setCreatedAt(LocalDateTime.now());

			List<CpuUsageSummary> summaryList = Collections.singletonList(cpuUsageSummary);

			// when
			Mockito.when(cpuUsageSummaryRepository.findByTypeAndCreatedAtBetween(
							any(Status.Type.class), any(LocalDateTime.class), any(LocalDateTime.class)))
					.thenReturn(summaryList);

			// then
			List<UsageResponse.Target> result = cpuUsageService.getHourlyCpuUsage(startDateTime, endDateTime);
			assertEquals(1, result.size());
			assertEquals("4.05%", result.get(0).getMaxUsage());
			assertEquals("1.83%", result.get(0).getAvgUsage());
			assertEquals("0.00%", result.get(0).getMinUsage());
		}

		@Test
		@DisplayName("조회 실패 테스트")
		void getHourlyCpuUsageFailTest() {
			// given
			LocalDateTime startDateTime = LocalDateTime.now().minusMonths(4);
			LocalDateTime endDateTime = LocalDateTime.now();

			// when

			// then
			assertThrows(OutOfBoundaryException.class, () -> cpuUsageService.getHourlyCpuUsage(startDateTime, endDateTime));
		}
	}

	@Nested
	@DisplayName("일 단위 CPU 사용량 조회 테스트")
	class dailyCpuUsageTest {

		@Test
		@DisplayName("조회 성공 테스트")
		void getDailyCpuUsageSuccessTest() {
			// given
			LocalDate startDate = LocalDate.now().minusMonths(11);
			LocalDate endDate = LocalDate.now();

			CpuUsageSummary cpuUsageSummary = CpuUsageSummary.builder()
					.type(Status.Type.DAYS)
					.maxUsage(0.04046215558992062)
					.avgUsage(0.018318524244537497)
					.minUsage(0.0)
					.build();

			cpuUsageSummary.setCreatedAt(LocalDateTime.now());

			List<CpuUsageSummary> summaryList = Collections.singletonList(cpuUsageSummary);

			// when
			Mockito.when(cpuUsageSummaryRepository.findByTypeAndCreatedAtBetween(
							any(Status.Type.class), any(LocalDateTime.class), any(LocalDateTime.class)))
					.thenReturn(summaryList);

			// then
			List<UsageResponse.Target> result = cpuUsageService.getDailyCpuUsage(startDate, endDate);
			assertEquals(1, result.size());
			assertEquals("4.05%", result.get(0).getMaxUsage());
			assertEquals("1.83%", result.get(0).getAvgUsage());
			assertEquals("0.00%", result.get(0).getMinUsage());
		}

		@Test
		@DisplayName("조회 실패 테스트")
		void getDailyCpuUsageFailTest() {
			// given
			LocalDate startDate = LocalDate.now().minusYears(2);
			LocalDate endDate = LocalDate.now();

			// when

			// then
			assertThrows(OutOfBoundaryException.class, () -> cpuUsageService.getDailyCpuUsage(startDate, endDate));
		}
	}
}
