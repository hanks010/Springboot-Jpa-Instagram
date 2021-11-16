package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer> {

	// :은 변수를 바인딩하기 위한 문법

	@Modifying // INSERT,DELETE,UPDATE 등의 데이터베이스에 변경을 주는 네이티브 쿼리에는 @Modifying이 필요
	@Query(value = "INSERT INTO subscribe(fromUserId,toUserId,createDate) VALUES (:fromUserId,:toUserId,now())", nativeQuery = true)
	void mSubscribe(int fromUserId, int toUserId); 
	// 변경된 행의 개수만큼 리턴, 실패하면 -1, 하지만 예외처리를 따로 해줄 것이기 때문에 리턴값을 쓰지는 않음

	@Modifying
	@Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
	void mUnSubscribe(int fromUserId, int toUserId);
}
