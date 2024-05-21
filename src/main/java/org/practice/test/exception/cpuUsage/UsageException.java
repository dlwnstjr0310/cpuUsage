package org.practice.test.exception.cpuUsage;

import lombok.Getter;
import org.practice.test.exception.Error;
import org.springframework.http.HttpStatus;

@Getter
public class UsageException extends RuntimeException {

	Error error;

	HttpStatus httpStatus;

	public UsageException(Error error, HttpStatus httpStatus) {
		super(error.getMessage());
		this.error = error;
		this.httpStatus = httpStatus;
	}
}
