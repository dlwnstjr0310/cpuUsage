package org.practice.test.exception.dataManagement;

import org.practice.test.exception.Error;
import org.springframework.http.HttpStatus;

public class UnexpectedDataException extends DataManagementException {
	public UnexpectedDataException() {
		super(Error.UNEXPECTED_DATA_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
