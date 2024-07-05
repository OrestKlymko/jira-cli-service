package org.swe.home.processor;

import org.supercsv.cellprocessor.FmtDate;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FmtLocalDate extends FmtDate {

	private final DateTimeFormatter formatter;

	public FmtLocalDate(DateTimeFormatter formatter) {
		super(formatter.toString());
		this.formatter = Objects.requireNonNull(formatter);
	}

	public FmtLocalDate(String dateFormat) {
		super(dateFormat);
		this.formatter = DateTimeFormatter.ofPattern(dateFormat);
	}


	@Override
	public Object execute(Object value, CsvContext context) {
		if (value == null) {
			return null;
		} else if (value instanceof LocalDateTime) {
			return ((LocalDateTime) value).format(formatter);
		} else {
			throw new SuperCsvCellProcessorException(LocalDateTime.class, value, context, this);
		}
	}
}
