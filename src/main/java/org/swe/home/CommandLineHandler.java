package org.swe.home;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.swe.home.exception.NotFoundException;
import org.swe.home.model.IssueRequest;
import org.swe.home.model.IssueResponse;
import org.swe.home.service.IssueService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
@AllArgsConstructor
public class CommandLineHandler implements CommandLineRunner {

	private final IssueService issueFacade;
	private final Scanner scanner = new Scanner(System.in);

	@Override
	public void run(String... args) {
		while (true) {
			System.out.println("Enter command (new, close, list, exit):");
			String command = scanner.nextLine();
			dispatcherCommand(command);
		}
	}

	public void dispatcherCommand(String command) {
		switch (command) {
			case "new":
				System.out.println("Enter parent issue id:");
				String parentId = scanner.nextLine();
				System.out.println("Enter description:");
				String description = scanner.nextLine();
				System.out.println("Enter link url to the log:");
				String link = scanner.nextLine();

				IssueRequest issueRequest = new IssueRequest(parentId, description, link);
				Long issueId = null;
				try {
					issueId = issueFacade.createIssue(issueRequest);
				} catch (IOException e) {
					System.out.println("Something problem: " + e.getMessage());
				}
				System.out.println("New issue created with ID: " + issueId);
				break;
			case "close":
				System.out.println("Enter issue id to close:");
				try {
					Long id = Long.parseLong(scanner.nextLine());
					issueFacade.closeIssue(id);
					System.out.println("Issue " + id + " closed.");
				} catch (NumberFormatException e) {
					System.out.println("You should write a number");
					dispatcherCommand("close");
				} catch (NotFoundException | IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			case "list":
				List<IssueResponse> openIssues = issueFacade.getAllIssue();
				if (openIssues.isEmpty()) {
					System.out.println("Please, add new record, DB is empty");
				}
				openIssues.forEach(issue -> {
					System.out.println(issue.id() + " | " + issue.description() + " | " + issue.parentId() + " | " + issue.status() + " | " + issue.creationTimestamp() + " | " + issue.link());
				});
				break;
			case "exit":
				System.exit(0);
			default:
				System.out.println("Unknown command");
		}
	}
}

