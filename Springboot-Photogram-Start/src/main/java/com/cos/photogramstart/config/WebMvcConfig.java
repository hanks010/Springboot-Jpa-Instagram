package com.cos.photogramstart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer { // web 설정 파일

	@Value("${file.path}")
	private String uploadFolder;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		
		// file:////Users/user/Documents/SpringBoot/upload/ yml에 설정된 경로로 바꿔준다 
		registry.addResourceHandler("/upload/**") // jsp 페이지에서 /upload/** 이런 주소 패턴이 나오면 발동 
				.addResourceLocations("file:///" + uploadFolder)//설정된 경로주소가 추가됨
				.setCachePeriod(60*10*6) //초단위 -> 1시간 동안 이미지 캐싱
				.resourceChain(true)
				.addResolver(new PathResourceResolver());
	}
}
