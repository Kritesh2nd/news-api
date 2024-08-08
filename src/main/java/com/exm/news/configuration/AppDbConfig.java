package com.exm.news.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        entityManagerFactoryRef = "appEntityManager",
        basePackages = "com.exm.news.repository.app",
        transactionManagerRef = "appTransactionManager"
)
public class AppDbConfig {
    
	@Bean("appDataSourceProperties")
	@ConfigurationProperties("spring.app.datasource")
	DataSourceProperties appDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean("appDataSource")
	@ConfigurationProperties("spring.app.datasource")
	DataSource primaryDataSource(
			@Qualifier("appDataSourceProperties")
			DataSourceProperties appDataSourceProperties) {
		return appDataSourceProperties
				.initializeDataSourceBuilder()
				.build();
	}
    
    @Bean("appEntityManager")
    LocalContainerEntityManagerFactoryBean appEntityManager(
			@Qualifier("appDataSource") DataSource dataSource,
			EntityManagerFactoryBuilder entityManagerFactoryBuilder) {
        Map<String, Object> jpaProperties = new HashMap<>();
        return entityManagerFactoryBuilder
                .dataSource(dataSource)
                .packages("com.exm.news.entity.app")
                .properties(jpaProperties)
                .build();
    }

    @Bean("appTransactionManager")
    PlatformTransactionManager appTransactionManager(
			@Qualifier("appEntityManager")LocalContainerEntityManagerFactoryBean
					entityManagerFactoryBean) {
        return new JpaTransactionManager(
				Objects.requireNonNull(entityManagerFactoryBean.getObject()));
    }

}