package com.exm.news.repository.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exm.news.entity.app.Comment;
@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long>{

}
