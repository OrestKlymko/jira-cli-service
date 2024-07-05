package org.swe.home.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.swe.home.entity.Issue;
import org.swe.home.enums.Status;
import org.swe.home.exception.NotFoundException;
import org.swe.home.model.IssueRequest;
import org.swe.home.model.IssueResponse;
import org.swe.home.repository.IssueRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IssueServiceTest {

	@Mock
	private IssueRepository issueRepository;

	@InjectMocks
	private IssueService issueService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void successCreateIssue() throws IOException {
		IssueRequest request = new IssueRequest("1L", "Test Description", "http://test.com");
		Issue issue = Issue.builder()
				.id(1L)
				.parentId("1L")
				.description("Test Description")
				.link("http://test.com")
				.creationTimestamp(LocalDateTime.now())
				.status(Status.OPEN)
				.build();

		when(issueRepository.createIssue(any(Issue.class))).thenReturn(issue);

		Long issueId = issueService.createIssue(request);

		assertNotNull(issueId);
		assertEquals(1L, issueId);
		verify(issueRepository, times(1)).createIssue(any(Issue.class));
	}

	@Test
	void successCloseIssue() throws IOException, NotFoundException {
		doNothing().when(issueRepository).closeIssue(1L);

		issueService.closeIssue(1L);

		verify(issueRepository, times(1)).closeIssue(1L);
	}

	@Test
	void successGetAllIssue() {
		Issue issue1 = Issue.builder()
				.id(1L)
				.parentId("1L")
				.description("Test Description 1")
				.link("http://test1.com")
				.creationTimestamp(LocalDateTime.now())
				.status(Status.OPEN)
				.build();
		Issue issue2 = Issue.builder()
				.id(2L)
				.parentId("2L")
				.description("Test Description 2")
				.link("http://test2.com")
				.creationTimestamp(LocalDateTime.now())
				.status(Status.OPEN)
				.build();

		List<Issue> issues = Arrays.asList(issue1, issue2);
		when(issueRepository.getAllIssue()).thenReturn(issues);

		List<IssueResponse> issueResponses = issueService.getAllIssue();

		assertNotNull(issueResponses);
		assertEquals(2, issueResponses.size());
		verify(issueRepository, times(1)).getAllIssue();
	}
}
