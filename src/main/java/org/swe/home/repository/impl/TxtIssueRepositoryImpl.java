package org.swe.home.repository.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.swe.home.entity.Issue;
import org.swe.home.enums.Status;
import org.swe.home.repository.IssueRepository;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TxtIssueRepositoryImpl implements IssueRepository {
	@Value("${txt.file.path}")
	private String txtFilePath;
	private static final String DELIMITER = "\t";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

	@Override
	public Issue createIssue(Issue issue) throws IOException {
		File file = Paths.get(txtFilePath).toFile();
		boolean fileExist = file.exists() && file.length() > 0;

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			long lastIssueId = getLastIssueId() != null ? getLastIssueId() : 0;
			issue.setId(lastIssueId + 1);
			if (!fileExist) {
				writer.write("ID" + DELIMITER + "Description" + DELIMITER + "ParentId" + DELIMITER + "Status" + DELIMITER + "CreationTimestamp" + DELIMITER + "Link");
				writer.newLine();
			}
			writer.write(formatIssue(issue));
			writer.newLine();
		}
		return issue;
	}

	private String formatIssue(Issue issue) {
		return issue.getId() + DELIMITER +
				issue.getDescription() + DELIMITER +
				issue.getParentId() + DELIMITER +
				issue.getStatus() + DELIMITER +
				issue.getCreationTimestamp().format(DATE_TIME_FORMATTER) + DELIMITER +
				issue.getLink();
	}

	@Override
	public void closeIssue(Long id) throws IOException {
		List<Issue> issues = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				Issue issue = parseIssue(line);
				if (issue.getId().equals(id)) {
					issue.setStatus(Status.CLOSED);
				}
				issues.add(issue);
			}
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFilePath))) {
			writer.write("ID" + DELIMITER + "Description" + DELIMITER + "ParentId" + DELIMITER + "Status" + DELIMITER + "CreationTimestamp" + DELIMITER + "Link");
			writer.newLine();
			for (Issue issue : issues) {
				writer.write(formatIssue(issue));
				writer.newLine();
			}
		}
	}

	private Issue parseIssue(String line) {
		String[] parts = line.split(DELIMITER);

		return new Issue.IssueBuilder()
				.id(Long.parseLong(parts[0]))
				.description(parts[1])
				.parentId(parts[2])
				.status(Status.toEnum(parts[3]))
				.creationTimestamp(LocalDateTime.parse(parts[4], DATE_TIME_FORMATTER))
				.link(parts[5])
				.build();
	}

	@Override
	public List<Issue> getAllIssue() {
		List<Issue> issues = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				Issue issue = parseIssue(line);
				if (issue.getStatus().equals(Status.OPEN)) {
					issues.add(issue);
				}
			}
		} catch (IOException e) {
			System.out.println("Db is empty or error: " + e.getMessage());
		}
		return issues;
	}

	public Long getLastIssueId() {
		Long lastId = null;
		try (BufferedReader reader = new BufferedReader(new FileReader(txtFilePath))) {
			String line;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				Issue issue = parseIssue(line);
				lastId = issue.getId();
			}
		} catch (IOException e) {
			System.out.println("Db is empty or error: " + e.getMessage());
		}
		return lastId;
	}
}
