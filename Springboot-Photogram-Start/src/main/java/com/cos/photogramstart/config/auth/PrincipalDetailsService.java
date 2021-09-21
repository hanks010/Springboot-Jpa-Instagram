package com.cos.photogramstart.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService{
	//SecurityConfig의 loginProcessingUrl에서 로그인 요청인지를 판단한 다음
	//로그인 프로세스가 진행될 때 내부적으로 IoC에 떠있던 UserDetailsService에서 낚아채서 진행을 하도록 돼있다.
	
	//그런데 PrincipalDetailsService클래스가 UserDetailsService를 상속하고 IoC에 등록을 했기 때문에
	//UserDetailsService 대신 PrincipalDetailsService에서 로그인 프로세스가 진행되게 된다. 
	//그리고 로그인이 진행될 때 loadUserByUsername 메서드가 실행된다.

	//1. 패스워드는 알아서 체킹하므로 신경쓸 필요 없다.
	//2. 리턴이 잘되면 자동으로 UserDetails을 세션으로 만든다
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			return null;
		}
		return new PrincipalDetails(userEntity);	
	}

}
