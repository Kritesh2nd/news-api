package com.exm.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NewsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsApiApplication.class, args);
		System.out.println("News Api Application");
	}

}

/*
main now
entity > repository -> interface -> User Details -> (Filters) -> (exception handling) -> service -> controller
drop database news; create database news; use news;
mvn spring-boot:run
*/
