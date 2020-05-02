package com.bid90.app.DTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class PostUploadDTO {
	String title;
	String description;
	List<MultipartFile> files;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<MultipartFile> getFiles() {
		return files;
	}
	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
	
	
}
