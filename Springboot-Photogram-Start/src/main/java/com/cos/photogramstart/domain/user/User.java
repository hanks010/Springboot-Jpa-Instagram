package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//JPA - Java Persistance API(자바로 데이터를 영구적으로 저장(DB애)할 수 있는 API를 제공

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity //디비에 테이블 생성
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //번호증가전략이 데이터베이스를 따라간다.
	private int id;
	@Column(length = 100, unique=true,nullable=false) //oAuth2 로그인을 위해 컬럼 길이 늘림
	private String username;
	@Column(nullable = false) 
	private String password;
	@Column(nullable = false)
	private String name;
	private String website;
	private String bio;
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl; //사진
	private String role; //권한
	
	
	//User를 Select할 때 해당 User id로 등록된 image들을 다 가져와
	//Lazy = User를 Select할 때 해당 User id로 등록된 image들을 가져오지마 - 대신 getImages 메서드의 image들이 호출될 때 가져와
	//Eager = User를 Select할 때 해당 User id로 등록된 image들을 전부 Join해서 가져와
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY)//나는 연관관계의 주인이 아니다. 테이블에 컬럼 만들지마 
	@JsonIgnoreProperties({"user"})
	private List<Image> images;  //양방향 매핑
	
	private LocalDateTime createDate;
	
	@PrePersist //디비에 INSERT 되기 직전에 실행
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
	
}
