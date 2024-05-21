package org.practice.test.exception.dataManagement;

import lombok.Getter;
import org.practice.test.exception.Error;
import org.springframework.http.HttpStatus;

@Getter
public class DataManagementException extends RuntimeException {

	Error error;

	HttpStatus httpStatus;

	public DataManagementException(Error error, HttpStatus httpStatus) {
		super(error.getMessage());
		this.error = error;
		this.httpStatus = httpStatus;
	}

}
