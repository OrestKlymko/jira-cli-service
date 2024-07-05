package org.swe.home.entity;

import jakarta.persistence.*;
import lombok.*;
import org.swe.home.enums.Status;

import java.time.LocalDateTime;

@Data
@Entity
public class Issue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	private String parentId;
	private LocalDateTime creationTimestamp;
	@Enumerated(EnumType.STRING)
	private Status status;
	private String link;

	public Issue(Long id,String parentId, String description, String link, LocalDateTime creationTimestamp, Status status) {
		this.id=id;
		this.parentId = parentId;
		this.description = description;
		this.link = link;
		this.creationTimestamp = creationTimestamp;
		this.status = status;
	}

	public Issue() {

	}

	public static IssueBuilder builder() {
		return new IssueBuilder();
	}

	public static class IssueBuilder {
		private Long id;
		private String parentId;
		private String description;
		private String link;
		private LocalDateTime creationTimestamp;
		private Status status;

		public IssueBuilder() {
		}

		public IssueBuilder id(Long id) {
			this.id = id;
			return this;
		}
		public IssueBuilder parentId(String parentId) {
			this.parentId = parentId;
			return this;
		}

		public IssueBuilder description(String description) {
			this.description = description;
			return this;
		}

		public IssueBuilder link(String link) {
			this.link = link;
			return this;
		}

		public IssueBuilder creationTimestamp(LocalDateTime creationTimestamp) {
			this.creationTimestamp = creationTimestamp;
			return this;
		}

		public IssueBuilder status(Status status) {
			this.status = status;
			return this;
		}

		public Issue build() {
			return new Issue(id,parentId, description, link, creationTimestamp, status);
		}
	}
}
