package com.sky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sky.entity.Issue;
import com.sky.entity.User;

public interface IssueRepository extends JpaRepository<Issue, Long>{

	List<Issue> findByProjectID(Long projectID);
	
    @Query("SELECT i FROM Issue i WHERE i.title LIKE %?1% AND i.status = ?2 AND i.priority = ?3 AND i.assignee.id = ?4")
    List<Issue> search(String title, String status, String priority, Long assigneeId);

    List<Issue> findByAssigneeId(Long assigneeId);

    @Query("SELECT i.assignee FROM Issue i WHERE i.id = ?1")
    List<User> findAssigneesByIssueId(Long issueId);
}
