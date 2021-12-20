package com.cos.photogramstart.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.service.SubscribeService;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;
	private final SubscribeService subscribeService;

	
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile,
			@AuthenticationPrincipal PrincipalDetails principalDetails){ //MultipartFile의 변수명이 jsp input 태그의 name과 같아야 한다.
		User userEntity = userService.profileImageUrlUpdate(principalId,profileImageFile);
		principalDetails.setUser(userEntity); //세션 변경
		return new ResponseEntity<>(new CMRespDto<>(1,"프로필 사진 변경 성공",null),HttpStatus.OK);	
	}
	
	@GetMapping("/api/user/{pageUserId}/subscribe") // 내가 이동한 페이지의 주인(pageUser)이 구독하고 있는 사람의 정보
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		List<SubscribeDto> subscribeDto = subscribeService.subscribeList(principalDetails.getUser().getId(),pageUserId);
		return new ResponseEntity<>(new CMRespDto<>(1,"구독자 정보 리스트 불러오기 성공",subscribeDto),HttpStatus.OK);
	}

	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(@Valid UserUpdateDto userUpdateDto, BindingResult bindingResult // 꼭 @Valid 애노테이션이 있는
																								// 파라미터 다음 파라미터에 위치해야 함
			, @PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		// System.out.println(userUpdateDto);


		if (bindingResult.hasErrors()) { 
			Map<String, String> errorMap = new HashMap<>(); 

			for (FieldError error : bindingResult.getFieldErrors()) { // getFieldErrors 컬렉션에 에러들을 모아준다
				errorMap.put(error.getField(), error.getDefaultMessage());
				System.out.println("=====================");
				System.out.println(error.getDefaultMessage());
	            System.out.println("=====================");
			}
			throw new CustomValidationApiException("유효성 검사 실패함",errorMap);
		}else {
			User userEntity = userService.UserUpdate(id, userUpdateDto.toEntity());
			principalDetails.setUser(userEntity); //세션정보 변경
			return new CMRespDto<>(1, "회원수정완료",userEntity);// 응답 시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답
		}
	}
}
