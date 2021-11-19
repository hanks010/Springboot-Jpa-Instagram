package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

	private final ImageRepository imageRepository;
	
	@Value("${file.path}") //org.springframework...
	private String uploadFolder; //여기에 yml에서 설정한 경로값이 담긴다.

	@Transactional
	public void upload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		// getOriginalFilename 메서드로 업로드된 파일의 이름을 받아온다.
		// 하지만 중복된 이름이 들어올 경우 덮어씌워지기 때문에 구분을 짓기 위해 UUID값을 붙여준다.
		UUID uuid = UUID.randomUUID(); // uuid
		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename(); // 1.jpg
		System.out.println("이미지 파일이름: "+imageFileName);
		
		//파일을 실제로 어디에 저장할 지에 대한 경로를 지정한다.
		Path imageFilePath = Paths.get(uploadFolder+imageFileName); //경로+파일이름 => 경로가 된다.
	
		//통신, I/O가 일어날 때는 예외가 발생할 수 있다. -> 예외처리 필수 
		try {
			Files.write(imageFilePath,imageUploadDto.getFile().getBytes()); //(파일경로, 바이트화한 실제 이미지 파일)
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		// image 테이블에 저장
		Image image = imageUploadDto.toEntity(imageFileName,principalDetails.getUser());
		imageRepository.save(image);
	}
}
