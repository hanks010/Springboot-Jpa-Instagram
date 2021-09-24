package com.cos.photogramstart.config.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService{
//페이스북에서 인증을 하고 돌려주는 응답(회원정보)을 처리할 것이다.
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	//System.out.println("OAuth2 서비스 탐");
	OAuth2User oauth2User = super.loadUser(userRequest);
	//System.out.println(oauth2User.getAttributes());
	//oauth2User는 Map 타입을 데이터를 들고있다.
	
	Map<String, Object> userInfo = oauth2User.getAttributes();
	String username ="facebook_"+ (String)userInfo.get("id"); //facebook 로그인의 고유 아이디로 만들어준다.
	String password = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
	String name = (String)userInfo.get("name");
	String email = (String)userInfo.get("email");

	User userEntity = userRepository.findByUsername(username);
	
	if(userEntity == null ){ //페이스북 최초 로그인
		User user = User.builder()
				.username(username)
				.password(password)
				.name(name)
				.email(email)
				.role("ROLE_USER")
				.build();
		
		return new PrincipalDetails(userRepository.save(user),userInfo);
	}else { //이미 페이스북으로 회원가입이 돼있다는 말
		return new PrincipalDetails(userEntity,userInfo);
	}
	}
}
