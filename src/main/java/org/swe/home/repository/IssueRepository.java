package org.swe.home.repository;

import org.swe.home.entity.Issue;
import org.swe.home.exception.NotFoundException;

import java.io.IOException;
import java.util.List;

public interface IssueRepository {
	Issue createIssue(Issue issue) throws IOException;

	void closeIssue(Long id) throws IOException, NotFoundException;

	List<Issue> getAllIssue();
}
