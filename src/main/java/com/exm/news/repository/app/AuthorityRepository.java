package com.exm.news.repository.app;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.exm.news.entity.app.Authority;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
	@Query(nativeQuery = true, value="SELECT * FROM authority a where a.id = :id")
	Authority findAuthorityById(Long id);
	
	@Query(nativeQuery = true, value="SELECT * FROM authority a where a.name = :name")
	Authority findAuthorityByName(String name);

} 