package com.wfiis.pz.project.monitor.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
/**
 * 
 * @author Mateusz Papież
 * 
 * Class that contains helping functions
 *
 */
public class Util{
	
	/**
	 * Converting string to timestamp
	 * 
	 * @param str_date date in format string to convert
	 * @return date in timestamp format
	 */
  public static Timestamp convertStringToTimestamp(String str_date) {
    try {
      DateFormat formatter;
      formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date date = (Date) formatter.parse(str_date);
      java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
 
      return timeStampDate;
    } catch (ParseException e) {
      System.out.println("Exception :" + e);
      return null;
    }
  }
  
  /**
   * 
   * Converting timestamp to string
   * 
   * @param ts date in timestamp format
   * @return date in string format
   */
  public static String convertTimestampToString(Timestamp ts){
	  
	  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	  String string  = dateFormat.format(ts);
	  
	  return string;
	  
  }
  
}