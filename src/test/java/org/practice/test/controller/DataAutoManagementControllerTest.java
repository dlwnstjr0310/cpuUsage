package org.practice.test.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.practice.test.domain.entity.CpuUsage;
import org.practice.test.repository.CpuUsageRepository;
import org.practice.test.repository.CpuUsageSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DataAutoManagementControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CpuUsageRepository cpuUsageRepository;
	@Autowired
	private CpuUsageSummaryRepository cpuUsageSummaryRepository;

	@BeforeEach
	void setUp() {

		CpuUsage cpuUsage = CpuUsage.builder()
				.usage(0.02345)
				.build();

		cpuUsage.setCreatedAt(LocalDateTime.now());

		cpuUsageRepository.save(cpuUsage);
	}

	@Test
	@DisplayName("분 단위 CPU 사용량 저장 테스트")
	void autoCreateCpuUsageTest() throws Exception {
		// given
		long beforeCount = cpuUsageRepository.count();

		// when
		mockMvc.perform(post("/data-management/minute"))
				.andExpect(status().isOk());

		// then
		assertThat(cpuUsageRepository.count()).isGreaterThan(beforeCount);
	}

	@Test
	@DisplayName("시간 단위 CPU 사용량 통계 및 저장 테스트")
	void autoCreateHourlyCpuUsageTest() throws Exception {
		// given
		long beforeCount = cpuUsageSummaryRepository.count();

		// when
		mockMvc.perform(post("/data-management/hourly"))
				.andExpect(status().isOk());

		// then
		assertThat(cpuUsageSummaryRepository.count()).isGreaterThan(beforeCount);
	}

	@Test
	@DisplayName("일 단위 CPU 사용량 통계 및 저장 테스트")
	void autoCreateDailyCpuUsageTest() throws Exception {
		// given
		long beforeCount = cpuUsageSummaryRepository.count();

		// when
		mockMvc.perform(post("/data-management/daily"))
				.andExpect(status().isOk());

		// then
		assertThat(cpuUsageSummaryRepository.count()).isGreaterThan(beforeCount);
	}
}
