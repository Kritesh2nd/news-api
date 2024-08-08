package com.exm.news.repository.app;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exm.news.entity.app.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


//	@Query(nativeQuery = true, value="SELECT * FROM users u where u.email = :email")
//	User findUserByEmail(String email);
//
//	@Query(nativeQuery = true, value="SELECT * FROM users u where u.email = :email")
//	Optional<User> findUserByEmailOptonal(String email);
	
	@Query(nativeQuery = true, value="SELECT * FROM users u where u.id = :id")
	User findUserById(Long id);
	
	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM users")
    int getUserCount();
	
} 