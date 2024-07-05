package org.swe.home.processor;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class ParseLong extends CellProcessorAdaptor {

	public ParseLong() {
		super();
	}

	@Override
	public Object execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);

		if (value instanceof Long) {
			return value;
		} else if (value instanceof String) {
			try {
				return Long.parseLong((String) value);
			} catch (NumberFormatException e) {
				throw new SuperCsvCellProcessorException(
						String.format("Value '%s' could not be parsed as a Long", value), context, this);
			}
		} else {
			throw new SuperCsvCellProcessorException(Long.class, value, context, this);
		}
	}
}