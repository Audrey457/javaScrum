package some_tools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Tools {
	
	/**
	 * Convert a String (hh:hh (a)(p)m MONTH DAY, aaaa) to a Date
	 * @param toConvert the String to convert
	 * @return an instance of java.sql.Date
	 */
	public static LocalDateTime stringToDate(String toConvert){
		toConvert = toConvert.replaceAll("Am", "AM");
		toConvert = toConvert.replaceAll("Pm", "PM");
		DateTimeFormatter format = DateTimeFormatter
				.ofPattern("hh:mm a MMMM d, yyyy", Locale.ENGLISH);
		return LocalDateTime.parse(toConvert, format);
	}
	
	
	/**
	 * YYYY-MM-DD HH:mm:ss
	 * @param ldt
	 * @return
	 */
	public static String toDateTimeSql(LocalDateTime ldt){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return ldt.format(formatter);
	}
	
	public static String stringDateToDateTimeSql(String toConvert){
		return toDateTimeSql(stringToDate(toConvert));
	}
}
