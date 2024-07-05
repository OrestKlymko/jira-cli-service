package org.swe.home.model;

public record IssueRequest(
		String parentId,
		String description,
		String link
) {

}
