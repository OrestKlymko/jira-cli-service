package org.swe.home.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.swe.home.entity.Issue;
import org.swe.home.enums.Status;
import org.swe.home.exception.NotFoundException;
import org.swe.home.model.IssueRequest;
import org.swe.home.model.IssueResponse;
import org.swe.home.repository.IssueRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class IssueService {
	private final IssueRepository issueRepository;

	public IssueService(@Qualifier("txtIssueRepositoryImpl") IssueRepository issueRepository) {
		this.issueRepository = issueRepository;
	}

	public Long createIssue(IssueRequest request) throws IOException {
		Issue issue = Issue.builder()
				.parentId(request.parentId())
				.description(request.description())
				.link(request.link())
				.creationTimestamp(LocalDateTime.now())
				.status(Status.OPEN)
				.build();
		return issueRepository.createIssue(issue).getId();
	}

	public void closeIssue(Long id) throws NotFoundException, IOException {
			issueRepository.closeIssue(id);
	}

	public List<IssueResponse> getAllIssue() {
		return issueRepository
				.getAllIssue()
				.stream()
				.map(IssueResponse::toResponse)
				.toList();
	}

}
