package com.cos.photogramstart.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

//어노테이션이 없어도 JpaRepository를 상속하면 IoC에 자동으로 등록이 된다.
public interface UserRepository extends JpaRepository<User, Integer>{
	//JPA query method
	//간단한 쿼리는 이름규칙만 맞게 쓰면 자동으로 찾아줌
	User findByUsername(String username);
}
