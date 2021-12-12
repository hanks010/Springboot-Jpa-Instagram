package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.likes.LikesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikesService {

	private final LikesRepository likesRepository;

	@Transactional
	public void like(int imageId, int principalId) {
		likesRepository.mLikes(imageId, principalId);
	}
	@Transactional
	public void unLike(int imageId, int principalId) {
		likesRepository.mUnLikes(imageId, principalId);
	}
}
