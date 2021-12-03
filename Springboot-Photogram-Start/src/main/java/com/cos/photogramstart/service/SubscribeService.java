package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repository는 EntityManager를 구현해서 만들어져 있는 구현체
	// EntityManager를 사용해 쿼리를 직접 구현할 것이다.

	@Transactional(readOnly = true)
	public List<SubscribeDto> subscribeList(int principalId, int pageUserId) {
		StringBuffer sb = new StringBuffer();

		// 쿼리 준비
		// 끝마다 한 칸씩 띄워줘야 함, FROM, WHERE절 뒤에는 띄어쓰기가 필요하기 때문
		sb.append("SELECT u.id, u.username, u.profileImageUrl, ");
		sb.append("if((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id),1,0) subscribeState, ");
		sb.append("if((?=u.id),1,0) equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId =?"); // 세미콜론 빼야함
		// 첫 번째 물음표 - principalId, 두 번째 물음표 - principalId, 세 번째 물음표 - pageUserId

		// 쿼리 완성
		Query query = em.createNativeQuery(sb.toString())
				.setParameter(1, principalId)
				.setParameter(2, principalId)
				.setParameter(3, pageUserId);
		// 쿼리 실행(qlrm 라이브러리 필요 <- DTO에 DB 결과를 맵핑하기 위해서
		
		//데이터베이스에서 리턴된 결과값을 자바 클래스에 맵핑해주는 라이브러리 -> qlrm 
		//리턴 받을 결과가 모델의 결과가 아닌 새로운 조합으로써 dto를 통해 받아야 한다면 JpaRepository에서 구현할 수 없다.
		
		JpaResultMapper result = new JpaResultMapper();
		
		// 한 건 리턴 시 result.uniqueResult() 메서드 사용
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class); // 여러 건 리턴, 두 번째 매개변수는 리턴받을 데이터 타입
		return subscribeDtos;
	}

	@Transactional
	public void subscribe(int fromUserId, int toUserId) {
		try {
			subscribeRepository.mSubscribe(fromUserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다");
		}
	}

	@Transactional
	public void unSubscribe(int fromUserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromUserId, toUserId);
	}
}
