package com.cos.photogramstart.handler.ex;

/**
 * @author user
 *
 */
public class CustomException extends RuntimeException { // RuntimeException 상속하여 커스텀한다.

	// 객체를 구분할 때
	private static final long serialVersionUID = 1L;

	public CustomException(String message) {
		super(message); 
	}
}
