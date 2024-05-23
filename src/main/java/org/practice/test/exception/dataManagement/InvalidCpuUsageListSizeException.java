package org.practice.test.exception.dataManagement;

import org.practice.test.exception.Error;
import org.springframework.http.HttpStatus;

public class InvalidCpuUsageListSizeException extends DataManagementException {
	public InvalidCpuUsageListSizeException() {
		super(Error.INVALID_CPU_USAGE_LIST_SIZE, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
