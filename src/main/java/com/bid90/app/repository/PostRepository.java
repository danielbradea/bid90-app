package com.bid90.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bid90.app.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{

	Post findPostById(Long i);

}
