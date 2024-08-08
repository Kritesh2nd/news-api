package com.exm.news.repository.auth;

import com.exm.news.entity.app.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import com.exm.news.entity.auth.Login;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    @Query(nativeQuery = true, value="SELECT * FROM logins l where l.id = :id")
    Login findLoginById(Long id);

    @Query(nativeQuery = true, value="SELECT * FROM logins l where l.email = :email")
    Login findLoginByEmail(String email);

    @Query(nativeQuery = true, value="SELECT * FROM login ORDER BY id DESC LIMIT 1")
    Article findLastLogin();

}