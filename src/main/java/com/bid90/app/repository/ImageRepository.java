package com.bid90.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bid90.app.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long>{

	Image findImabeById(Long i);
	Image findImageByName(String name);

}
