package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}")
	private String uploadFolder;

	@Transactional
	public User profileImageUrlUpdate(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID(); 
		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename();
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			throw new CustomApiException("유저를 찾을 수 없습니다.");
			});
		userEntity.setProfileImageUrl(imageFileName); //앞 경로는 /upload/** 패턴이 대신 붙여줌 
		
		return userEntity;
	} //더티체킹으로 업데이트 
	
	@Transactional
	public User UserUpdate(int id, User user) {
		// 1. 영속화
		User userEntity = userRepository.findById(id).orElseThrow(() -> {// new Supplier<IllegalArgumentException>() {
			// @Override
			// public IllegalArgumentException get() {
			return new CustomValidationApiException("찾을 수 없는 id입니다");
			// }
		});

		// 2. 영속화된 오브젝트를 수정 - 더티체킹(업데이트 완료)
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword); // 해쉬화

		userEntity.setName(user.getName());
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());

		return userEntity;
	}// 더티체킹이 일어나서 수정이 완료됨

	@Transactional(readOnly = true)
	public UserProfileDto profile(int pageUserId, int principalId) {

		UserProfileDto dto = new UserProfileDto();

		// SELECT*FROM image WHERE userId = :userId;
		User userEntity = userRepository.findById(pageUserId).orElseThrow(() -> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다");
		});
		dto.setUser(userEntity);
		dto.setImageCount(userEntity.getImages().size());
		dto.setPageOwnerState(pageUserId == principalId); // 1은 페이지 주인, -1은 주인 아님

		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		dto.setSubscribeCount(subscribeCount);
		dto.setSubscribeState(subscribeState == 1);

		//좋아요 카운트 추가, 서버에서 다 만들어서 가지고 감 
		userEntity.getImages().forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
		});
		return dto;
	}

}
