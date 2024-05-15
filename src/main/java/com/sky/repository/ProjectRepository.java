package com.sky.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sky.entity.Project;
import com.sky.entity.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {

//	List<Project> findByOwner(User user);

//	@Query("SELECT p FROM Project p JOIN p.team t WHERE t=:user")
//	List<Project> findByTeam(@Param("user") User user);

	List<Project> findByNameContainsAndTeamContains(String partialName, User user);

	List<Project> findByTeamContainsOrOwner(User user, User owner);
}
