package com.cos.photogramstart.handler.ex;

import java.util.Map;

/**
 * @author user
 *
 */
public class CustomValidationException extends RuntimeException { // RuntimeException 상속하여 커스텀한다.

	// 객체를 구분할 때
	private static final long serialVersionUID = 1L;

	private Map<String, String> errorMap;

	public CustomValidationException(String message, Map<String, String> errorMap) {
		super(message); // message를 부모에게 넘겨주면 된다.
		this.errorMap = errorMap;
	}

	public Map<String, String> getErrorMap() {
		return errorMap;
	}

}
