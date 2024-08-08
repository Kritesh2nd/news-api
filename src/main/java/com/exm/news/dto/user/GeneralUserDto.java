package com.exm.news.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class GeneralUserDto {

    private Long userId;

    private String username;

    private String email;

    private String firstName;
    
    private String lastName;

    private List<String> role;

}
