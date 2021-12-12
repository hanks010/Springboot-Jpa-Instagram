package com.cos.photogramstart.web.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.service.LikesService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ImageApiController {

	private final ImageService imageService;
	private final LikesService likesService;

	@GetMapping("/api/image")
	public ResponseEntity<?> imageStory(@AuthenticationPrincipal PrincipalDetails principalDetails,
			@PageableDefault(size = 3) Pageable pageable) { // 최신 데이터가 앞으로
		Page<Image> images = imageService.mStory(principalDetails.getUser().getId(), pageable);
		return new ResponseEntity<>(new CMRespDto<>(1, "이미지 불러오기 성공", images), HttpStatus.OK);
		//Image 객체가 리턴 될 때 메시지 컨버터가 작동해서 모든 getter 메소드가 실행된다.
		//Image -> Likes -> Image 무한참조 발생, @JsonIgnoreProperties 추가 필요
	}

	@PostMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> likes(@PathVariable int imageId,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		likesService.like(imageId, principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 성공", null), HttpStatus.CREATED);
	}

	@DeleteMapping("/api/image/{imageId}/likes")
	public ResponseEntity<?> unLikes(@PathVariable int imageId,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		likesService.unLike(imageId, principalDetails.getUser().getId());
		return new ResponseEntity<>(new CMRespDto<>(1, "좋아요 취소 성공", null), HttpStatus.OK);
	}
}
