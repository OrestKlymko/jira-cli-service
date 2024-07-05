package org.swe.home.enums;

public enum Status {
	OPEN("Open"),
	CLOSED("Closed");

	private final String name;

	Status(String status) {
		name = status;
	}

	public String toString() {
		return this.name;
	}

	public static Status toEnum(String name) {
		switch (name) {
			case "Open":
				return Status.OPEN;
			default:
				return Status.CLOSED;
		}
	}
}
