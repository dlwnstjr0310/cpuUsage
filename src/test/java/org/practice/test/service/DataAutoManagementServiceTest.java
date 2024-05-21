package org.practice.test.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.practice.test.domain.entity.CpuUsage;
import org.practice.test.domain.entity.CpuUsageSummary;
import org.practice.test.exception.dataManagement.UnexpectedDataException;
import org.practice.test.repository.CpuUsageRepository;
import org.practice.test.repository.CpuUsageSummaryRepository;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class DataAutoManagementServiceTest {

	@Mock
	private CpuUsageRepository cpuUsageRepository;

	@Mock
	private CpuUsageSummaryRepository cpuUsageSummaryRepository;

	@InjectMocks
	private DataAutoManagementService dataAutoManagementService;

	@Test
	@DisplayName("CPU 사용량 자동삭제 테스트")
	void autoDeleteCpuUsageTest() {
		// given

		// when
		dataAutoManagementService.autoDeleteExpiredData();

		// then
		verify(cpuUsageRepository, times(1)).deleteByCreatedAtBefore(any());
		verify(cpuUsageSummaryRepository, times(2)).deleteByTypeIsAndCreatedAtBefore(any(), any());
	}

	@Nested
	@DisplayName("CPU 사용량 자동저장 테스트")
	class autoCreateCpuUsageTest {

		@Test
		@DisplayName("분 단위 데이터 저장 성공 테스트")
		void autoCreateCpuUsageSuccessTest() {
			// given

			// when
			dataAutoManagementService.autoCreateCpuUsage();

			// then
			ArgumentCaptor<CpuUsage> captor = ArgumentCaptor.forClass(CpuUsage.class);

			verify(cpuUsageRepository, times(1)).save(captor.capture());
		}

		@Test
		@DisplayName("분 단위 데이터 자동저장 실패 테스트")
		void autoCreateCpuUsageFailTest() {
			// given
			CpuUsage cpuUsage = CpuUsage.builder()
					.usage(0.005)
					.build();

			cpuUsage.setCreatedAt(LocalDateTime.now());

			// when
			when(cpuUsageRepository.save(cpuUsage)).thenThrow(new RuntimeException());

			// then
			assertThrows(UnexpectedDataException.class, () -> dataAutoManagementService.autoCreateCpuUsage());
		}

		@Test
		@DisplayName("시간 단위 데이터 저장 테스트")
		void autoCreateHourlyCpuUsageTest() {
			// given

			// when
			dataAutoManagementService.autoCreateHourlyCpuUsage();

			// then
			ArgumentCaptor<CpuUsageSummary> summaryArgumentCaptor = ArgumentCaptor.forClass(CpuUsageSummary.class);

			verify(cpuUsageRepository, times(1)).findByCreatedAtBetween(any(), any());
			verify(cpuUsageSummaryRepository, times(1)).save(summaryArgumentCaptor.capture());
		}

		@Test
		@DisplayName("일 단위 데이터 저장 테스트")
		void autoCreateDailyCpuUsageTest() {
			// given

			// when
			dataAutoManagementService.autoCreateDailyCpuUsage();

			// then
			ArgumentCaptor<CpuUsageSummary> summaryArgumentCaptor = ArgumentCaptor.forClass(CpuUsageSummary.class);

			verify(cpuUsageRepository, times(1)).findByCreatedAtBetween(any(), any());
			verify(cpuUsageSummaryRepository, times(1)).save(summaryArgumentCaptor.capture());
		}
	}
}
