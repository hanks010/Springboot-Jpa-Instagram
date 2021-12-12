package com.cos.photogramstart.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "likes_uk", // 유니크 제약조건의 이름
		columnNames = { "imageId", "userId" } // 두 개를 복합적으로 건다, 데이터베이스의 컬럼명
) })
public class Likes { // N
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JoinColumn(name = "imageId")
	@ManyToOne
	private Image image; // 1
	//@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user; // 1
	
	private LocalDateTime createDate;

	@PrePersist
	public void creteDate() {
		this.createDate = LocalDateTime.now();
	}

}
