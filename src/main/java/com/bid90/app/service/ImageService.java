package com.bid90.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bid90.app.model.Image;
import com.bid90.app.repository.ImageRepository;

@Service
public class ImageService implements CRUDService<Image>{

	@Autowired
	ImageRepository imageRepository;
	
	@Override
	public Image create(Image t) {
		Image newImage = new Image();
		newImage.setPath(t.getPath());
		return imageRepository.save(newImage);
	}

	@Override
	public Image reade(Long i) {
		// TODO Auto-generated method stub
		return imageRepository.findImabeById(i);
	}

	@Override
	public Image update(Image t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Image t) {
		imageRepository.delete(t);
		
	}

	public Image findByName(String name) {
		// TODO Auto-generated method stub
		return imageRepository.findImageByName(name);
	}

}
