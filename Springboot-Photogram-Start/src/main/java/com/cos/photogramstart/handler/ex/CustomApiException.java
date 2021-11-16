package com.cos.photogramstart.handler.ex;

/**
 * @author user
 *
 */
public class CustomApiException extends RuntimeException { // RuntimeException 상속하여 커스텀한다.

	// 객체를 구분할 때
	private static final long serialVersionUID = 1L;

	public CustomApiException(String message) {
		super(message); 
	}
}
