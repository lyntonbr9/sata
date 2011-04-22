package sata.domain.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SATAUtil {
	
	public static Timestamp getTimeStampPeriodoCotacao(String periodo){
		Calendar cal = new GregorianCalendar();
		cal.set(Integer.valueOf(periodo.substring(0,4)), Integer.valueOf(periodo.substring(4,6)) - 1, Integer.valueOf(periodo.substring(6,8)),0,0,0);
		cal.set(Calendar.MILLISECOND, 0);
		Timestamp ts = new Timestamp(cal.getTime().getTime());
		return ts;
	}
	
	public static String getTimeStampFormatado(Timestamp ts, boolean comHora){
		Calendar cal = new GregorianCalendar();
		cal.setTime(ts);
		String tempoFormatado = "";
		if (comHora == false)
			tempoFormatado = cal.get(Calendar.DAY_OF_MONTH)+ "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
		else
			tempoFormatado = cal.get(Calendar.DAY_OF_MONTH)+ "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR)
				+ " " + cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		return tempoFormatado;
	}

}
