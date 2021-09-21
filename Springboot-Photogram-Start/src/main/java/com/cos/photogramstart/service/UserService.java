package com.cos.photogramstart.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional
	public User UserUpdate(int id, User user) {
		//1. 영속화
		User userEntity = userRepository.findById(id).orElseThrow(()->{//new Supplier<IllegalArgumentException>() {
		//@Override
		//public IllegalArgumentException get() {
			return new CustomValidationApiException("찾을 수 없는 id입니다");
		//}
		}); 
		
		//2. 영속화된 오브젝트를 수정 - 더티체킹(업데이트 완료)
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword); //해쉬화
		
		userEntity.setName(user.getName());
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		
		return userEntity;
	}//더티체킹이 일어나서 수정이 완료됨
}
