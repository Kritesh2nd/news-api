package com.exm.news.customRepo.auth;

import com.exm.news.customRepo.auth.interfaces.LoginRepo;
import com.exm.news.entity.auth.Login;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional("authTransactionManager")
@Repository
public class LoginRepoImplement implements LoginRepo {

    @PersistenceContext(unitName = "authEntityManager")
    private EntityManager entityManager;

    @Override
    public List<Login> findLoginSameEmail(String email) {
        String sql = "SELECT * FROM logins WHERE email = :email";
        Query query = entityManager.createNativeQuery(sql, Login.class);
        query.setParameter("email", email);

        return query.getResultList();
    }
}
