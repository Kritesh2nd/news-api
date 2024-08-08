package com.exm.news.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "authEntityManager",
        basePackages = "com.exm.news.repository.auth",
        transactionManagerRef = "authTransactionManager"
)
public class AuthDbConfig {
	
	@Primary
	@Bean("authDataSourceProperties")
	@ConfigurationProperties("spring.auth.datasource")
	DataSourceProperties authDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean("authDataSource")
	@ConfigurationProperties("spring.auth.datasource")
	DataSource primaryDataSource(@Qualifier("authDataSourceProperties") DataSourceProperties authDataSourceProperties) {
		return authDataSourceProperties
				.initializeDataSourceBuilder()
				.build();
	}

    @Primary
    @Bean("authEntityManager")
    LocalContainerEntityManagerFactoryBean authEntityManager(@Qualifier("authDataSource") DataSource dataSource, EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        Map<String, Object> jpaProperties = new HashMap<>();
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.exm.news.entity.auth")
                .persistenceUnit("logins")
                .properties(jpaProperties)
                .build();
    }

    @Primary
    @Bean("authTransactionManager")
    PlatformTransactionManager authTransactionManager(@Qualifier("authEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }

}