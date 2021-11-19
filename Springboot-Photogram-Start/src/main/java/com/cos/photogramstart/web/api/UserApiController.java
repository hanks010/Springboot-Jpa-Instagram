package com.cos.photogramstart.web.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.service.UserService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.user.UserUpdateDto;

@RestController
public class UserApiController {

	@Autowired
	private UserService userService;
	
	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(@Valid UserUpdateDto userUpdateDto,
			BindingResult bindingResult //꼭 @Valid 애노테이션이 있는 파라미터 다음 파라미터에 위치해야 함
			,@PathVariable int id, @AuthenticationPrincipal PrincipalDetails principalDetails) {
	System.out.println(userUpdateDto);
	
		User userEntity = userService.UserUpdate(id, userUpdateDto.toEntity());
		principalDetails.setUser(userEntity); //세션정보 변경
		return new CMRespDto<>(1, "회원수정완료",userEntity); //응답 시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답
	}
}
