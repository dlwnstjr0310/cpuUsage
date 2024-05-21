package org.practice.test.exception.cpuUsage;

import org.practice.test.exception.Error;
import org.springframework.http.HttpStatus;

public class OutOfBoundaryException extends UsageException {
	public OutOfBoundaryException() {
		super(Error.OUT_OF_BOUNDARY, HttpStatus.BAD_REQUEST);
	}
}
