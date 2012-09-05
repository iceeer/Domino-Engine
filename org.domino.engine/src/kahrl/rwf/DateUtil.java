package kahrl.rwf;

import java.util.Calendar;

public class DateUtil {

		
public static String getCurrentDate(){
	java.util.Date date = new java.util.Date(System.currentTimeMillis());
	return date.toString();
	}

public static String getCurrentInternationalDate(){
	Calendar calendar = Calendar.getInstance();
	String month = "Dec";
	switch (calendar.get(Calendar.MONTH)){
	case 0:
		month="Jan";
		break;
	case 1:
		month="Feb";
		break;
	case 2:
		month="Mar";
		break;
	case 3:
		month="Apr";
		break;
	case 4:
		month="May";
		break;
	case 5:
		month="Jun";
		break;
	case 6:
		month="Jul";
		break;
	case 7:
		month="Aug";
		break;
	case 8:
		month="Sep";
		break;
	case 9:
		month="Oct";
		break;
	case 10:
		month="Nov";
		break;
	}
return calendar.get(Calendar.DAY_OF_MONTH) + "-" + month + "-" + calendar.get(Calendar.YEAR);
}

	
}
