package org.practice.test.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.practice.test.domain.entity.CpuUsage;
import org.practice.test.repository.CpuUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CpuUsageControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CpuUsageRepository cpuUsageRepository;

	@BeforeEach
	void setUp() {

		CpuUsage cpuUsage = CpuUsage.builder()
				.usage(0.02345)
				.build();

		cpuUsage.setCreatedAt(LocalDateTime.now());

		cpuUsageRepository.save(cpuUsage);
	}

	@Test
	@DisplayName("분 단위 사용량 조회 테스트")
	void getMinuteCpuUsageTest() throws Exception {
		// given
		LocalDateTime startDateTime = LocalDateTime.now().minusDays(5);
		LocalDateTime endDateTime = LocalDateTime.now();

		// when
		ResultActions perform = mockMvc.perform(get("/cpu-usage/minute")
				.param("startDateTime", startDateTime.toString())
				.param("endDateTime", endDateTime.toString()));

		// then
		perform.andExpect(status().isOk())
				.andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
	}

	@Test
	@DisplayName("시간 단위 사용량 조회 테스트")
	void getHourlyCpuUsageTest() throws Exception {
		// given
		LocalDateTime startDateTime = LocalDateTime.now().minusMonths(2);
		LocalDateTime endDateTime = LocalDateTime.now();

		// when
		ResultActions perform = mockMvc.perform(get("/cpu-usage/hour")
				.param("startDateTime", startDateTime.toString())
				.param("endDateTime", endDateTime.toString()));

		// then
		perform.andExpect(status().isOk())
				.andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
	}

	@Test
	@DisplayName("일 단위 사용량 조회 테스트")
	void getDailyCpuUsageTest() throws Exception {
		// given
		LocalDate startDate = LocalDate.now().minusMonths(10);
		LocalDate endDate = LocalDate.now();

		// when
		ResultActions perform = mockMvc.perform(get("/cpu-usage/day")
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString()));

		// then
		perform.andExpect(status().isOk())
				.andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(0))));
	}
}
