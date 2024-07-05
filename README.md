# JIRA CLI Service

## Description

This project implements a simple repository for saving and managing `Issue` records in CSV format. The project uses Spring Boot to simplify development and structure the application.

## Requirements

- Java 17 or higher
- Maven 3.6.3 or higher

## Setup

1. **Clone the repository**

```sh
git clone https://github.com/OrestKlymko/jira-cli-service.git
cd jira-cli-service
```

2. **Build the Project**

```sh
mvn clean install
```

3. **Run the Project**

```sh
java -jar target/jira-cli-service-1.0.0.jar
```

When you run the project, you will see a prompt to enter commands. The available commands are:

1. **new** - Create a new issue
2. **close** - Close an existing issue
3. **list** - List all open issues
4. **exit** - Exit the application
