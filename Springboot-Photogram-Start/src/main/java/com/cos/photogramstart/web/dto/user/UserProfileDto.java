package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
	private boolean PageOwnerState; // 변수명에 is 붙으면 jstl 문법에서 파싱이 잘 안된다.
	private int imageCount; //사진 개수
	private User user;
}
