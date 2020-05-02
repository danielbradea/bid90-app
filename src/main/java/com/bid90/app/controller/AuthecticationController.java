package com.bid90.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bid90.app.DTO.LoginDTO;
import com.bid90.app.DTO.RegisterDTO;
import com.bid90.app.DTO.UserInfoDTO;
import com.bid90.app.exception.CustomException;
import com.bid90.app.model.User;
import com.bid90.app.security.JwtProvider;
import com.bid90.app.service.UserService;

@RestController
public class AuthecticationController {

	@Autowired
	UserService userService;

	
	@GetMapping("/")
	public String home() {
		return "bid90";
	}
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	ModelMapper modelMapper;
	
	@PostMapping("/api/auth/register")
	public UserInfoDTO register(@RequestBody RegisterDTO registerDTO) {
		User userToSave = userService.findUserByEmail(registerDTO.getEmail());
		if(userToSave != null) {
			throw new CustomException("Email exist", HttpStatus.BAD_REQUEST);
		}
		userToSave = new User();
		userToSave.setEmail(registerDTO.getEmail());
		userToSave.setPassword(registerDTO.getPassword());
		userToSave.setName(registerDTO.getName());
		userToSave.setUserRole("ROLE_USER");
		User userAdded = userService.create(userToSave);
		
		UserInfoDTO userAddedInfo = modelMapper.map(userAdded,UserInfoDTO.class);
		
		return userAddedInfo;

	}
	
	
	@PostMapping("/api/auth/login")
	public Map<String, Object> login(@RequestBody LoginDTO loginDTO) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		User user = userService.findUserByEmail(loginDTO.getEmail());
		if(user == null || !userService.PassworMatches(loginDTO.getPassword(), user.getPassword())) {
			
			throw new CustomException("Wrong email or password ", HttpStatus.BAD_REQUEST);
		}
		
		String token = jwtProvider.loginAndGetToken(user.getEmail(),loginDTO.getPassword());
		response.put("token", token);
		
		return response;
	}
	
}
