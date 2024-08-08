package com.exm.news.customRepo.auth.interfaces;

import com.exm.news.entity.auth.Login;

import java.util.List;

public interface LoginRepo {
    List<Login> findLoginSameEmail(String email);
}
