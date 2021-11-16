package com.cos.photogramstart.domain.subscribe;

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

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = {
		@UniqueConstraint(
				name = "subscribe_uk", //유니크 제약조건의 이름
				columnNames = { "fromUserId", "toUserId" } //두 개를 복합적으로 건다, 데이터베이스의 컬럼명
				) 
		}
)
public class Subscribe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JoinColumn(name = "fromUserId") // 데이터베이스 컬럼명 지정
	@ManyToOne
	private User fromUser;

	@JoinColumn(name = "ToUserId")
	@ManyToOne
	private User toUser;

	private LocalDateTime createDate;

	@PrePersist
	public void creteDate() {
		this.createDate = LocalDateTime.now();
	}
}
