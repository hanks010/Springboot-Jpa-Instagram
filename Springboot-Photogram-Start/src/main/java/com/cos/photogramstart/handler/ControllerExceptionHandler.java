package com.cos.photogramstart.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;

@RestController
@ControllerAdvice //이 애노테이션을 붙이면 모든 익셉션을 다 낚아챈다
public class ControllerExceptionHandler {

	@ExceptionHandler(CustomValidationException.class) //모든 CustomValidationException을 이 함수가 가로챔
	public String validationException(CustomValidationException e) {
		// CMRespDto, Script 비교
		// 1. 클라이언트에게 응답해줄 때는 Scrip가 좋음
		// 2. Ajax통신 - CMRespDto
		// 3. Android 통신 - CMRespDto
		
	
		//return new CMRespDto<Map<String,String>>(-1, e.getMessage(),e.getErrorMap());
		return Script.back(e.getErrorMap().toString());
	}
	

	@ExceptionHandler(CustomValidationApiException.class) //모든 CustomValidationApiException을 이 함수가 가로챔
	public ResponseEntity<?> validationException(CustomValidationApiException e) {
		return new ResponseEntity<>(new CMRespDto<Map<String,String>>(-1, e.getMessage(),e.getErrorMap()),HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(CustomApiException.class) //모든 CustomValidationApiException을 이 함수가 가로챔
	public ResponseEntity<?> apiException(CustomApiException e) {
		return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null),HttpStatus.BAD_REQUEST);
	}
}
