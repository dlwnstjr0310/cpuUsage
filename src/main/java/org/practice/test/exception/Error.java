package org.practice.test.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Error {

	NOT_CORRECT_CHOICE(1000, "정확한 날짜를 선택해주세요."),
	OUT_OF_BOUNDARY(1001, "조회할 수 있는 범위를 벗어났습니다."),

	UNEXPECTED_DATA_ERROR(2000, "CPU 사용량 저장중 예기치 않은 오류가 발생했습니다."),
	INVALID_CPU_USAGE_LIST_SIZE(2001, "분당 CPU 사용량 데이터가 충분하지 않습니다.");

	final Integer code;

	final String message;
}
