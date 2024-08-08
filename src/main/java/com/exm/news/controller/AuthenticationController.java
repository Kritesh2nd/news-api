package com.exm.news.controller;

import com.exm.news.dto.user.LoginUserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.exm.news.service.UserServiceImplement;

import jakarta.validation.Valid;

import com.exm.news.constant.PathConstant;
import com.exm.news.response.LoginResponse;
import com.exm.news.dto.user.RegisterUserDto;
import com.exm.news.entity.auth.Login;
import com.exm.news.repository.auth.LoginRepository;
import com.exm.news.response.BasicResponseDto;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

	@Autowired
	private UserServiceImplement userService;
	
	@Autowired
	private LoginRepository loginRepository;
	
	@GetMapping("cat")
	public String cat() {
		return "auth cat";
	}


	@GetMapping("getEmail")
	public List<LoginUserDto> getEmail(@RequestBody String email){
		return userService.findLoginByEmailCus(email);
	}

	@GetMapping("userinfo")
	public List<Login> userinfo() {
		return loginRepository.findAll();
	}
	
	@PreAuthorize("hasAnyAuthority('admin')")
	@GetMapping("admin")
	public String admin() {
		return "Admin access";
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor')")
	@GetMapping("editor")
	public String editor() {
		return "Editor access";
	}
	
	@PreAuthorize("hasAnyAuthority('admin','editor','reader')")
	@GetMapping("reader")
	public String reader() {
		return "Reader access";
	}
	
	@PostMapping(PathConstant.SIGNUP)
    public ResponseEntity<BasicResponseDto> register(@RequestBody @Valid RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(userService.signup(registerUserDto));
    }

    @PostMapping(PathConstant.LOGIN)
    public ResponseEntity<LoginResponse> authenticate() {
		System.out.println("login test");
		return ResponseEntity.ok(userService.getUserToken());
    }
    
    @PreAuthorize("hasAnyAuthority('admin','editor','reader')")
    @GetMapping(PathConstant.ME)
    public ResponseEntity<?> authenticatedUser() {
        return ResponseEntity.ok(userService.getMe());
    }

}
