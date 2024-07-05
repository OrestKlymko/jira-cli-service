package org.swe.home.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.swe.home.entity.Issue;
import org.swe.home.enums.Status;

import java.time.LocalDateTime;

@Builder
public record IssueResponse(
		@JsonProperty("ID")
		Long id,
		@JsonProperty("Description")
		String description,
		@JsonProperty("ParentId")
		String parentId,
		@JsonProperty("Status")
		String status,
		@JsonProperty("CreationTimestamp")
		LocalDateTime creationTimestamp,
		@JsonProperty("Link")
		String link
) {

	public static IssueResponse toResponse(Issue issue) {
		return IssueResponse.builder()
				.id(issue.getId())
				.parentId(issue.getParentId())
				.description(issue.getParentId())
				.link(issue.getLink())
				.creationTimestamp(issue.getCreationTimestamp())
				.status(issue.getStatus().toString())
				.build();
	}
}
