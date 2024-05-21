package org.practice.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.practice.test.model.response.Response;
import org.practice.test.service.DataAutoManagementService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data-management")
@Tag(name = "Data Auto Management API", description = "CPU 사용량을 자동 저장하는 API")
public class DataAutoManagementController {

	private final DataAutoManagementService dataAutoManagementService;

	@PostMapping("/minute")
	@Operation(summary = "분 단위 사용량 저장", description = """
			매 분마다 자동 실행되어 CPU 사용량을 0~1 사이의 값으로 저장합니다. \n
			(0: 사용하지 않음, 1: 최대 사용량, 백분율 표기시 * 100) \n
			""")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "500", description = "CPU 사용량 저장중 예기치 않은 오류가 발생했습니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	public Response<Void> autoCreateCpuUsage() {

		dataAutoManagementService.autoCreateCpuUsage();

		return Response.<Void>builder()
				.build();
	}

	@PostMapping("/hourly")
	@Operation(summary = "시간 단위 사용량 통계 및 저장", description = "매 시간마다 자동 실행되어 해당 시간 CPU 의 최대/평균/최소 사용량을 저장합니다.")
	@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Response.class)))
	public Response<Void> autoCreateHourlyCpuUsage() {

		dataAutoManagementService.autoCreateHourlyCpuUsage();

		return Response.<Void>builder()
				.build();
	}

	@PostMapping("/daily")
	@Operation(summary = "일 단위 사용량 통계 및 저장", description = "매일 자정에 자동 실행되어 전날 CPU 의 최대/평균/최소 사용량을 저장합니다.")
	@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Response.class)))
	public Response<Void> autoCreateDailyCpuUsage() {

		dataAutoManagementService.autoCreateDailyCpuUsage();

		return Response.<Void>builder()
				.build();
	}

	@DeleteMapping
	@Operation(summary = "저장 기간이 지난 데이터 삭제", description = """
			매일 자정에 자동 실행되어 저장 기간이 지난 데이터를 삭제합니다. \n
			데이터의 종류에 따라 다른 기간이 적용됩니다. \n
			(분 단위: 7일, 시간 단위: 3개월, 일 단위: 1년)
			""")
	@ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Response.class)))
	public Response<Void> autoDeleteExpiredData() {

		dataAutoManagementService.autoDeleteExpiredData();

		return Response.<Void>builder()
				.build();
	}
}
