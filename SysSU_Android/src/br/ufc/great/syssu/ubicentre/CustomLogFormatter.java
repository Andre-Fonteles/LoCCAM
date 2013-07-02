package br.ufc.great.syssu.ubicentre;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLogFormatter extends Formatter{

	@Override
	public String format(LogRecord record) {
		return MessageFormat.format(
			"{0}: [{1}] - {2}", new Date(record.getMillis()), record.getLevel(), record.getMessage()); 
	}

}
