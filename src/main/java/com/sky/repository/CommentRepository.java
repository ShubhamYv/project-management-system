package com.sky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findByIssueId(Long issueId);
}
