package com.cos.photogramstart.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.handler.ex.CustomValidationException;

@Component // RestCOntroller, Service와 같은 애노테이션들이 Component를 상속해서 만들어져 있음
@Aspect // AOP 처리를 할 수 있는 핸들러
public class ValidationAdvice {

	// 특정 함수가 실행되기 직전에 실행시키고 싶으면 @Before 애노테이션
	// 특정 함수가 실행된 이후에 실행시키고 싶으면 @After 애노테이션
	// 특정 함수가 실행되기 직전에 실행된 뒤 그 함수가 종료될 때까지 관여하고 싶을 때는 @Around 애노테이션을 쓴다
	@Around("execution(* com.cos.photogramstart.web.api.*Controller.*(..))") // 값으로는 언제 동작할지에 대한 주소
	// 맨 앞의 *은 접근제한자를 의미, com.cos.photogramstart.web.api 패키지 안의 Controller로 끝나는 클래스의
	// 모든 메서드에 적용
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		// proceedingJoinPoint는 특정 함수의 모든 곳에 접근할 수 있는 변수이다
		// 이 메서드가 실행될 때 특정 함수의 모든 정보들을 proceedingJoinPoint에 담고 그 함수가 실행되기 전에 이 함수가 먼저
		// 실행된다

		//1. Dto 만들고
		//2. 모델에서 유효성 검사 애노테이션 추가
		//3. 파라미터에서 @Valid 애노테이션 추가 및 BindingResult 추가
		System.out.println("web api controller==============================");
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			// System.out.println(arg);
			if (arg instanceof BindingResult) {
				System.out.println("유효성 검사를 하는 함수임다");
				BindingResult bindingResult = (BindingResult) arg;
				if (bindingResult.hasErrors()) { // bindingResult에 에러가 하나라도 있으면
					Map<String, String> errorMap = new HashMap<>(); // hashMap에 모두 담고

					for (FieldError error : bindingResult.getFieldErrors()) { // getFieldErrors 컬렉션에 에러들을 모아준다
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println("=====================");
						System.out.println(error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성 검사 실패함", errorMap); //throw가 던져지면 아래 코드는 무효화된다
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 그 특정 함수로 다시 돌아가라는 의미, 이 때 특정 함수가 실행된다.

	}

	@Around("execution(* com.cos.photogramstart.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("web controller==============================");
		Object[] args = proceedingJoinPoint.getArgs();
		// 특정함수의 매개변수에 접근
		for (Object arg : args) {
			// System.out.println(arg);
			if (arg instanceof BindingResult) {
				System.out.println("유효성 검사를 하는 함수임다");
				BindingResult bindingResult = (BindingResult) arg;
				if (bindingResult.hasErrors()) { // bindingResult에 에러가 하나라도 있으면
					Map<String, String> errorMap = new HashMap<>(); // hashMap에 모두 담고

					for (FieldError error : bindingResult.getFieldErrors()) { // getFieldErrors 컬렉션에 에러들을 모아준다
						errorMap.put(error.getField(), error.getDefaultMessage());
						System.out.println("=====================");
						System.out.println(error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성 검사 실패함", errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed();
	}
}
