package com.cos.photogramstart.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.AuthService;
import com.cos.photogramstart.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드를 DI할 때 사용
@Controller // 1. IoC 2. 파일을 리턴하는 컨트롤러
public class AuthController {

	private final AuthService authService;

	// @Autowired
	// private AuthService authService;

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@GetMapping("/auth/signin")
	public String signinForm() {
		return "/auth/signin";
	}

	@PostMapping("/auth/signin")
	public String signin() {
		return "";
	}

	@GetMapping("/auth/signup")
	public String signupForm() {
		return "/auth/signup";
	}

	// 회원가입 버튼 -> /auth/signup -> /auth/signin
	@PostMapping("/auth/signup")
	// @ResponseBody
	//유효성 검사 중 하나라도 실패하면 bindingResult에 모두 담긴다.
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) { // key = value
																					// (x-www-form-urlencoded)
		// 일반적으로 컨트롤러는 파일을 반환하지만 반환자료형 앞에 @ResponseBody 애노테이션을 붙여주면 데이터를 반환한다.

		if (bindingResult.hasErrors()) { //bindingResult에 에러가 하나라도 있으면
			Map<String, String> errorMap = new HashMap<>(); //hashMap에 모두 담고

			for (FieldError error : bindingResult.getFieldErrors()) { // getFieldErrors 컬렉션에 에러들을 모아준다
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println("=====================");
				System.out.println(error.getDefaultMessage());
			}
			throw new CustomValidationException("유효성 검사 실패함",errorMap); //오류페이지가 아닌 원하는 메시지를 보여주기 위해 Exception을 throw해주는데 이 때 
			// RuntimeException를 사용하면 위의 getDefaultMessage를 띄울 수는 없고 문자열만 띄울 수 있다.
			//그래서 핸들러를 만들어 커스텀으로 예외 처리를 해줄 것이다.
			//그 데이터를 던진다
		} else {
			log.info(signupDto.toString());
			// User <- SignupDto
			User user = signupDto.toEntity();
			log.info(user.toString());

			User userEntity = authService.signUp(user);
			System.out.println(userEntity);
			return "/auth/signin";
		}
		
	}
	

}
