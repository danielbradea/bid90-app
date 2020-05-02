package com.bid90.app.model;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "post")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String title;
	String description;
	
	@OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
	@JsonManagedReference
	List<Image> image = new ArrayList<>();
	
	public Post() {
		super();
	}

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

	public List<Image> getImage() {
		return image;
	}
	public void removeImage(Image image) {
		this.image.remove(image);
		image.setPost(null);
	}
	public void setImage(Image image) {
		this.image.add(image);
		image.setPost(this);
	}

	public void setImages(List<Image> newlistImages) {
		this.image = newlistImages;
		
	}

}
