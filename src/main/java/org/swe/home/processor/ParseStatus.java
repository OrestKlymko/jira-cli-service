package org.swe.home.processor;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;
import org.swe.home.enums.Status;

public class ParseStatus extends CellProcessorAdaptor {

	public ParseStatus() {
		super();
	}

	@Override
	public Object execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);

		String strValue = value.toString();
		try {
			return Status.valueOf(strValue.toUpperCase());
		} catch (IllegalArgumentException e) {
			return
			System.out.printf("Value '%s' could not be parsed as a Status%n", strValue);
		}
	}
}
