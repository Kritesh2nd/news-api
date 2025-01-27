package com.exm.news.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasicResponseDto {

	private String message;

	private boolean success;
	
}
