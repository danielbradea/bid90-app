package com.bid90.app.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bid90.app.model.User;
import com.bid90.app.repository.UserRepository;

@Component
public class UserService implements CRUDService<User> , UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public User create(User t) {
		User newUser = new User();
		newUser.setId(0L);
		newUser.setUserRole(t.getUserRole());
		newUser.setEmail(t.getEmail());
		newUser.setName(t.getName());
		newUser.setPassword(bCryptPasswordEncoder.encode(t.getPassword()));
		return userRepository.save(newUser);
	}

	@Override
	public User reade(Long i) {
		return userRepository.findUserById(i);
	}

	@Override
	public User update(User t) {
		User userUpdate = userRepository.findUserById(t.getId());
		if (userUpdate == null) {
			userUpdate = new User();
		}
		userUpdate.setEmail(t.getEmail());
		userUpdate.setPassword(t.getPassword());
		userUpdate.setName(t.getName());
		return userRepository.save(userUpdate);
	}

	@Override
	public void delete(User t) {
		userRepository.delete(t);
	}

	public User findUserByEmail(String email) {
		
		return userRepository.findUserByEmail(email);
	}

	public User findEmailAndPassword(String email, String password) {
		
		return userRepository.findUserByEmailAndPassword(email,bCryptPasswordEncoder.encode(password));
	}

	
	public Boolean PassworMatches(CharSequence rawPassword, String encodedPassword) {
		return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findUserByEmail(email);
		if (user != null) {
			return buildUserForAuthentication(user);
		} else {
			throw new UsernameNotFoundException("Username not found");
		}
	}

	private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getUserRole());
		return authorities;
	}

	private UserDetails buildUserForAuthentication(User user) {
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				getAuthorities(user));
	}
}
