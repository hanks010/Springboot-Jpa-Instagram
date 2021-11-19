package com.cos.photogramstart.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class ImageUploadDto {
	//MultipartFile 타입에는 @Not Blank 유효성 검사가 지원이 안된다.
	private MultipartFile file;
	private String caption;
	
	public Image toEntity(String postImagUrl,User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImagUrl) //정확한 경로가 들어가야함
				.user(user)
				.build();
	}
}
