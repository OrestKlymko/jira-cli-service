package org.swe.home.repository.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import org.swe.home.entity.Issue;
import org.swe.home.enums.Status;
import org.swe.home.processor.*;
import org.swe.home.repository.IssueRepository;

import java.io.*;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CsvIssueRepositoryImpl implements IssueRepository {
	@Value("${csv.file.path}")
	private String csvFilePath;
	private static final String[] HEADER = {"ID", "Description", "ParentId", "Status", "CreationTimestamp", "Link"};
	private static final String[] NAME_MAPPING = {"id", "description", "parentId", "status", "creationTimestamp", "link"};

	@Override
	public Issue createIssue(Issue issue) throws IOException {
		File file = Paths.get(csvFilePath).toFile();
		boolean fileExist = file.length() == 0;

		try (ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(file, true),
				CsvPreference.STANDARD_PREFERENCE)) {
			CellProcessor[] processors = getWritingProcessor();
			if (fileExist) {
				beanWriter.writeHeader(HEADER);
			}
			long lastIssueId = getLastIssueId() != null ? getLastIssueId() : -1;
			issue.setId(lastIssueId + 1);
			beanWriter.write(issue, NAME_MAPPING, processors);
		}
		return issue;
	}

	private CellProcessor[] getWritingProcessor() {
		return new CellProcessor[]{
				null,
				null,
				null,
				new FmtStatus(),
				new FmtLocalDate(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
				null
		};
	}

	@Override
	public void closeIssue(Long id) throws IOException {
		List<Issue> issues = new ArrayList<>();

		try (ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(csvFilePath),
				CsvPreference.STANDARD_PREFERENCE)) {
			CellProcessor[] processors = getReadingProcessor();
			Issue issue;
			beanReader.getHeader(true);
			while ((issue = beanReader.read(Issue.class, NAME_MAPPING, processors)) != null) {
				if (issue.getId().equals(id)) {
					issue.setStatus(Status.CLOSED);
				}
				issues.add(issue);
			}
		}

		try (ICsvBeanWriter beanWriter = new CsvBeanWriter(new FileWriter(csvFilePath),
				CsvPreference.STANDARD_PREFERENCE)) {
			CellProcessor[] processors = getWritingProcessor();
			beanWriter.writeHeader(HEADER);
			for (Issue issue : issues) {
				beanWriter.write(issue, NAME_MAPPING, processors);
			}
		}
	}

	private CellProcessor[] getReadingProcessor() {
		return new CellProcessor[]{
				new ParseLong(),
				null,
				null,
				new ParseStatus(),
				new ParseLocalDateTime("yyyy-MM-dd'T'HH:mm"),
				null
		};
	}

	@Override
	public List<Issue> getAllIssue() {
		List<Issue> issues = new ArrayList<>();
		try (ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(csvFilePath),
				CsvPreference.STANDARD_PREFERENCE)) {
			CellProcessor[] processors = getReadingProcessor();
			beanReader.getHeader(true);
			Issue issue;
			while ((issue = beanReader.read(Issue.class, NAME_MAPPING, processors)) != null) {
				if(issue.getStatus().equals(Status.OPEN)){
					issues.add(issue);
				}
			}
		} catch (IOException e) {
			System.out.println("Db is empty");
		} catch (SuperCsvCellProcessorException e) {
			System.out.println("CSV processing error: " + e.getMessage());
		} catch (SuperCsvException e) {
			System.out.println("CSV error: " + e.getMessage());
		}
		return issues;
	}

	public Long getLastIssueId() {
		Long lastId = null;
		try (ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(csvFilePath),
				CsvPreference.STANDARD_PREFERENCE)) {
			CellProcessor[] processors = getReadingProcessor();
			beanReader.getHeader(true);
			Issue issue;
			while ((issue = beanReader.read(Issue.class, NAME_MAPPING, processors)) != null) {
				lastId = issue.getId();
			}
		} catch (IOException e) {
			System.out.println("Db is empty");
		} catch (SuperCsvCellProcessorException e) {
			System.out.println("CSV processing error: " + e.getMessage());
		} catch (SuperCsvException e) {
			System.out.println("CSV error: " + e.getMessage());
		}
		return lastId;
	}


}
