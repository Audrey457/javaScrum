package sometools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTools {
	
	//Private constructor to hide the implicite one
	private DateTools(){
		
	}
	
	/**
	 * Convert a String (hh:hh (a)(p)m MONTH DAY, aaaa) to a Date
	 * @param toConvert the String to convert
	 * @return an instance of java.sql.Date
	 */
	public static LocalDateTime stringToDate(String toConvert){
		String tmp = toConvert.replaceAll("am", "AM");;
		tmp = tmp.replaceAll("pm", "PM");
		DateTimeFormatter format = DateTimeFormatter
				.ofPattern("hh:mm a MMMM d, yyyy", Locale.ENGLISH);
		return LocalDateTime.parse(tmp, format);
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
