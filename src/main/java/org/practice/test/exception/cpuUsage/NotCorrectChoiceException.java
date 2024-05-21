package org.practice.test.exception.cpuUsage;

import org.practice.test.exception.Error;
import org.springframework.http.HttpStatus;

public class NotCorrectChoiceException extends UsageException {
	public NotCorrectChoiceException() {
		super(Error.NOT_CORRECT_CHOICE, HttpStatus.BAD_REQUEST);
	}
}
