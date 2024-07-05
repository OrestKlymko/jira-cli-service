package org.swe.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.swe.home.entity.Issue;
import org.swe.home.enums.Status;

import java.util.List;

@Repository
public interface JpaIssueRepository extends JpaRepository<Issue, Long> {
	List<Issue> findByStatus(Status status);
}
