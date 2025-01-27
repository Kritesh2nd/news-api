package com.exm.news.configuration;

import com.exm.news.entity.app.Authority;
import com.exm.news.entity.app.Category;
import com.exm.news.entity.app.User;
import com.exm.news.entity.auth.Login;
import com.exm.news.repository.app.*;
import com.exm.news.repository.auth.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class DatabasePopulate {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleImageRepository articleImageRepository;

    @Bean
    CommandLineRunner initDatabase() {
        DatabasePopulate d = new DatabasePopulate();
        return args -> {
//            d.populateCategory(categoryRepository);
//            d.populateAuthority(authorityRepository);
//            d.populateUser(userRepository, loginRepository, authorityRepository);
//            d.populateArticle(articleRepository, articleImageRepository, categoryRepository, userRepository);
        };
    }

    public void populateCategory(CategoryRepository categoryRepository) {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category("business"));
            categoryRepository.save(new Category("economy"));
            categoryRepository.save(new Category("entertainment"));
            categoryRepository.save(new Category("health"));
            categoryRepository.save(new Category("national"));
            categoryRepository.save(new Category("politics"));
            categoryRepository.save(new Category("sports"));
        }
    }

    public void populateAuthority(AuthorityRepository authorityRepository) {
        if (authorityRepository.count() == 0) {
            authorityRepository.save(new Authority("admin"));
            authorityRepository.save(new Authority("editor"));
            authorityRepository.save(new Authority("reader"));
        }

    }

    public void populateUser(UserRepository userRepository, LoginRepository loginRepository, AuthorityRepository authorityRepository) {
        if (userRepository.count() == 0) {

            String password = passwordEncoder.encode("password");
            Authority authorityAdmin = authorityRepository.findAuthorityByName("admin");
            Authority authorityEditor = authorityRepository.findAuthorityByName("editor");
            Authority authorityReader = authorityRepository.findAuthorityByName("reader");

            Set<Authority> authoritySet = new HashSet<>();
            authoritySet.add(authorityAdmin);
            authoritySet.add(authorityEditor);
            authoritySet.add(authorityReader);

            loginRepository.save(new Login("kritesh@gmail.com", password));
            userRepository.save(new User("kritesh", "Kritesh", "Thapa", authoritySet));

            authoritySet.clear();
            authoritySet.add(authorityEditor);
            authoritySet.add(authorityReader);

            loginRepository.save(new Login("manzil@gmail.com", password));
            userRepository.save(new User("manzil", "Manzil", "Shakya", authoritySet));

            loginRepository.save(new Login("rizul@gmail.com", password));
            userRepository.save(new User("rizul", "Rizul", "Shrestha", authoritySet));

            loginRepository.save(new Login("sandeep@gmail.com", password));
            userRepository.save(new User("sandeep", "Sandeep", "Rai", authoritySet));


            authoritySet.clear();
            authoritySet.add(authorityReader);

            loginRepository.save(new Login("ram@gmail.com", password));
            userRepository.save(new User("ram", "Ram", "Shakya", authoritySet));

            loginRepository.save(new Login("shyam@gmail.com", password));
            userRepository.save(new User("shyam", "Shyam", "Shrestha", authoritySet));

            loginRepository.save(new Login("hari@gmail.com", password));
            userRepository.save(new User("hari", "Hari", "Rai", authoritySet));

        }
    }

    public void populateArticle(ArticleRepository articleRepository, ArticleImageRepository articleImageRepository, CategoryRepository categoryRepository, UserRepository userRepository) {
        if (articleRepository.count() == 0) {

        }
    }
}
