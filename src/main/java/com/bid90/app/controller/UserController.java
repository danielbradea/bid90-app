package com.bid90.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;


@RestController
public class UserController {
	
	@ApiOperation(value = "Admin" , authorizations = {@Authorization(value = "Bearer")})
	@PostMapping("/admin/hello")
	public String admin() {
		return "Admin";
	}
	
	@ApiOperation(value = "User" , authorizations = {@Authorization(value = "Bearer")})
	@PostMapping("/user/hello")
	public String user() {
		
		
		return "User";
	}
}
