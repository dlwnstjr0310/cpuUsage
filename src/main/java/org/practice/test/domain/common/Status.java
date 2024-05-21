package org.practice.test.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Status {

	@Getter
	@AllArgsConstructor
	public enum Type {

		HOURS("시간 단위"),
		DAYS("일 단위");

		private final String description;
	}

}
