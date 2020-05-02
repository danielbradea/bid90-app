package com.bid90.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bid90.app.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findUserByEmail(String email);

	User findUserById(Long i);

	User findUserByEmailAndPassword(String email, String password);

}
