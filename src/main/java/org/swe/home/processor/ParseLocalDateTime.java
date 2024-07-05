package org.swe.home.processor;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ParseLocalDateTime extends CellProcessorAdaptor {
	private final DateTimeFormatter formatter;

	public ParseLocalDateTime(String dateFormat) {
		super();
		this.formatter = DateTimeFormatter.ofPattern(dateFormat);
	}

	@Override
	public Object execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);

		if (value instanceof Date) {
			Date date = (Date) value;
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		} else if (value instanceof String) {
			return LocalDateTime.parse((String) value, formatter);
		} else {
			throw new SuperCsvCellProcessorException(
					String.format("Value '%s' could not be parsed as a LocalDateTime", value), context, this);
		}
	}
}

