package com.cos.photogramstart.web.dto.subscribe;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {
	private int id; // 구독 및 구독취소할 누군가의 정보
	private String username;
	private String profileImageUrl;
	private BigInteger subscribeState; // 구독여부 확인
	private BigInteger equalUserState; // 로그인한 사용자와 같은지 확인
}
