package com.bid90.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bid90.app.model.Post;
import com.bid90.app.repository.PostRepository;

@Service
public class PostService implements CRUDService<Post>{

	@Autowired
	PostRepository postRepository;
	
	@Override
	public Post create(Post t) {
		
		return postRepository.save(t);
	}

	@Override
	public Post reade(Long i) {
		return postRepository.findPostById(i);
	}

	@Override
	public Post update(Post t) {
		Post updatedPost = postRepository.findPostById(t.getId());
		if(updatedPost == null) {
			return null;
		}
		updatedPost.setTitle(t.getTitle());
		updatedPost.setDescription(t.getDescription());
		
		return postRepository.save(updatedPost);
	}

	public Post updateImage(Post t) {
		Post updatedPost = postRepository.findPostById(t.getId());
		if(updatedPost == null) {
			return null;
		}
		updatedPost.setImages(t.getImage());
		
		return postRepository.save(updatedPost);
	}
	
	@Override
	public void delete(Post t) {
		postRepository.delete(t);
	}

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	
	
	

}
