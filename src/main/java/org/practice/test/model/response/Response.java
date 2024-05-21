package org.practice.test.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Response<T> {

	@Builder.Default
	Integer code = HttpStatus.OK.value();

	@Builder.Default
	String message = HttpStatus.OK.getReasonPhrase();

	T data;

}
