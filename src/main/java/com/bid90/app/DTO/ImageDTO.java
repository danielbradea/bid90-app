package com.bid90.app.DTO;

public class ImageDTO {
	
	Long id;
	String name;
	Long size;
	String link;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getLink() {
		return link+name;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
