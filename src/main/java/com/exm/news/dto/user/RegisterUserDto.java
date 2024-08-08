package com.exm.news.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
	
	@NotBlank(message = "input field cannot be blank")
    private String username;

    @Email(message = "invalid email format")
    private String email;
 
    @NotBlank(message = "input field cannot be blank")
    private String firstName;

    @NotBlank(message = "input field cannot be blank")
    private String lastName;
 
    @NotBlank(message = "input field cannot be blank")
    private String password;

}
