package org.practice.test.exception;

import lombok.RequiredArgsConstructor;
import org.practice.test.exception.cpuUsage.UsageException;
import org.practice.test.exception.dataManagement.DataManagementException;
import org.practice.test.model.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(basePackages = {"org.practice.test.controller"})
public class CommonExceptionHandler {

	@ExceptionHandler(UsageException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Response<Void> usageExceptionHandler(UsageException e) {

		return Response.<Void>builder()
				.code(e.getError().getCode())
				.message(e.getError().getMessage())
				.build();
	}

	@ExceptionHandler(DataManagementException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Response<Void> dataManagementExceptionHandler(DataManagementException e) {

		return Response.<Void>builder()
				.code(e.getError().getCode())
				.message(e.getError().getMessage())
				.build();
	}
}
