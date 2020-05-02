package com.bid90.app.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bid90.app.model.Image;
import com.bid90.app.service.ImageService;



@RestController
@RequestMapping("api/file")
public class FileController {

	@Autowired
	ImageService imageService;
	
	
	@RequestMapping(value = "/get/image",method = RequestMethod.GET ,produces = {MediaType.IMAGE_PNG_VALUE,MediaType.IMAGE_JPEG_VALUE})
	public  byte[] getFile(@RequestParam(name = "name") String name) throws IOException {
	
			Image image = imageService.findByName(name);
			if(image == null) {
				return null;
			}
		    InputStream in = new FileInputStream(image.getPath());
		    return IOUtils.toByteArray(in);
	}
	
}
