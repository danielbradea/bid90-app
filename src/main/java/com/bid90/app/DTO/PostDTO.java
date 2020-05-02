package com.bid90.app.DTO;

import java.util.List;

public class PostDTO {
	
	Long id;
	String title;
	String description;
	List<ImageDTO> image;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public List<ImageDTO> getImage() {
		return image;
	}
	public void setImage(List<ImageDTO> image) {
		this.image = image;
	}
	
}
