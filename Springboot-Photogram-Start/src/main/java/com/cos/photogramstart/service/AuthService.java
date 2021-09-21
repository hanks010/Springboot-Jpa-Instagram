package com.cos.photogramstart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

@Service //1.IoC 2. 트랜잭션 관리
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional//Write(Insert,Update,Delete)
	public User signUp(User user) {
		//회원가입 진행
	 //매개변수의 user는 통신을 통해서 받아온 데이터를 user 오브젝트에 담은 것이고
	 // save 한 뒤 반환된 user 객체는 DB에 저장된 데이터를 user 오브젝트에 담은 것이다. 
		
	 String rawPassword = user.getPassword();
	 String encPassword = bCryptPasswordEncoder.encode(rawPassword);
	 user.setPassword(encPassword);
	 user.setRole("ROLE_USER"); //관리자 ROLE_ADMIN
	 
	 User userEntity = userRepository.save(user);
	 return userEntity;
	}
}
