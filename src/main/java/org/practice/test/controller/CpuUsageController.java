package org.practice.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.practice.test.model.response.Response;
import org.practice.test.model.response.UsageResponse;
import org.practice.test.service.CpuUsageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cpu-usage")
@Tag(name = "Cpu Usage API", description = "CPU 사용량을 조회하는 API")
public class CpuUsageController {

	private final CpuUsageService cpuUsageService;

	@GetMapping("/minute")
	@Operation(summary = "분 단위 사용량 조회", description = """
			지정한 구간의 분 단위 CPU 사용률을 조회합니다. \n
			단, 7일 이전의 데이터는 조회할 수 없습니다.
			""")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UsageResponse.Base.class))),
			@ApiResponse(responseCode = "400", description = """
					1.조회할 수 있는 범위를 벗어났습니다. \n
					2.정확한 날짜를 선택해주세요.
					""", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	public Response<List<UsageResponse.Base>> getMinuteCpuUsage(
			@RequestParam @Schema(description = "시작일시", example = "2024-05-01T00:00:00", type = "string")
			LocalDateTime startDateTime,
			@RequestParam @Schema(description = "종료일시", example = "2024-05-30T12:00:00", type = "string")
			LocalDateTime endDateTime) {

		return Response.<List<UsageResponse.Base>>builder()
				.data(cpuUsageService.getMinuteCpuUsage(startDateTime, endDateTime))
				.build();
	}

	@GetMapping("/hour")
	@Operation(summary = "시간 단위 사용량 조회", description = """
			지정한 구간의 시간 단위 CPU 사용률을 조회합니다. \n
			단, 3개월 이전의 데이터는 조회할 수 없습니다.
			""")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UsageResponse.Target.class))),
			@ApiResponse(responseCode = "400", description = """
					1.조회할 수 있는 범위를 벗어났습니다. \n
					2.정확한 날짜를 선택해주세요.
					""", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	public Response<List<UsageResponse.Target>> getHourlyCpuUsage(
			@RequestParam @Schema(description = "시작일시", example = "2024-05-01T00:00:00", type = "string")
			LocalDateTime startDateTime,
			@RequestParam @Schema(description = "종료일시", example = "2024-05-30T12:00:00", type = "string")
			LocalDateTime endDateTime) {

		return Response.<List<UsageResponse.Target>>builder()
				.data(cpuUsageService.getHourlyCpuUsage(startDateTime, endDateTime))
				.build();
	}

	@GetMapping("/day")
	@Operation(summary = "일 단위 사용량 조회", description = """
			지정한 구간의 일 단위 CPU 사용률을 조회합니다. \n
			단, 1년 이전의 데이터는 조회할 수 없습니다.
			""")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UsageResponse.Target.class))),
			@ApiResponse(responseCode = "400", description = """
					1.조회할 수 있는 범위를 벗어났습니다. \n
					2.정확한 날짜를 선택해주세요.
					""", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	public Response<List<UsageResponse.Target>> getDailyCpuUsage(
			@RequestParam @Schema(description = "시작일", example = "2024-05-30", type = "string")
			LocalDate startDate,
			@RequestParam @Schema(description = "종료일", example = "2024-08-15", type = "string")
			LocalDate endDate) {

		return Response.<List<UsageResponse.Target>>builder()
				.data(cpuUsageService.getDailyCpuUsage(startDate, endDate))
				.build();
	}

}
