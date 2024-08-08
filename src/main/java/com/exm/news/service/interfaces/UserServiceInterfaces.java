package com.exm.news.service.interfaces;

import java.util.List;

import com.exm.news.dto.user.LoginUserDto;
import com.exm.news.response.LoginResponse;
import com.exm.news.dto.user.GeneralUserDto;
import com.exm.news.dto.user.RegisterUserDto;
import com.exm.news.response.BasicResponseDto;
import com.exm.news.dto.user.UpdateAuthorityDto;
import com.exm.news.dto.user.UpdateUserDto;
import com.exm.news.security.authentication.UserAuth;

public interface UserServiceInterfaces {

//	C[R]UD
	public List<GeneralUserDto> getGeneralUserList();
	public GeneralUserDto getGeneralUserById(Long id);
	public UserAuth authenticate(LoginUserDto input);
	public LoginResponse getUserToken();
	public GeneralUserDto getMe();
	
//	[C]RUD
	public BasicResponseDto signup(RegisterUserDto newUserData);
	
//	CR[U]D
	public BasicResponseDto updateUser(UpdateUserDto newUserData);
	public BasicResponseDto updateUserPassword(Long id,String password);
	public BasicResponseDto updateUserAuthority(UpdateAuthorityDto userAuthority);
	
//	CRU[D]
	public BasicResponseDto deleteMyAccount();
	public BasicResponseDto removeUserAuthority(UpdateAuthorityDto userAuthority);
	
}
