package org.swe.home.processor;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;
import org.swe.home.enums.Status;

public class FmtStatus extends CellProcessorAdaptor {
	public FmtStatus() {
		super();
	}

	@Override
	public String execute(Object value, CsvContext context) {
		if (value == null) {
			return null;
		} else if (value instanceof Status) {
			return value.toString();
		} else {
			throw new SuperCsvCellProcessorException(Status.class, value, context, this);
		}
	}


}

