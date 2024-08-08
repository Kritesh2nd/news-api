package com.exm.news.entity.app;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

	@NotBlank(message="please enter data")
    @Column(name = "username", nullable = false)
    private String username;

	@NotBlank(message="please enter data")
    @Column(name = "first_name")
    private String firstName;

	@NotBlank(message="please enter data")
    @Column(name = "last_name")
    private String lastName;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_authorities",
	 	joinColumns = @JoinColumn(name = "user_id"),
	 	inverseJoinColumns = @JoinColumn(name = "authority_id"))
	private Set<Authority> authorities;
	
}
