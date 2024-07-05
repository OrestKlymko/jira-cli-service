package org.swe.home.repository.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.swe.home.entity.Issue;
import org.swe.home.enums.Status;
import org.swe.home.exception.NotFoundException;
import org.swe.home.repository.IssueRepository;
import org.swe.home.repository.JpaIssueRepository;

import java.util.List;

@Repository
@AllArgsConstructor
public class JpaIssueRepositoryImpl implements IssueRepository {

	private final ApplicationContext applicationContext;

	private JpaIssueRepository getRepository() {
		return applicationContext.getBean(JpaIssueRepository.class);
	}

	@Override
	public Issue createIssue(Issue issue) {
		return getRepository().save(issue);
	}

	@Override
	public void closeIssue(Long id) throws NotFoundException {
		Issue issue = getRepository().findById(id)
				.orElseThrow(() -> new NotFoundException(String.format("Issue with id %s not found", id)));
		issue.setStatus(Status.CLOSED);
		getRepository().save(issue);
	}

	@Override
	public List<Issue> getAllIssue() {
		return getRepository().findByStatus(Status.OPEN);
	}
}
