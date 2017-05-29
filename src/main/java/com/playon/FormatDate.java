package com.playon;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class FormatDate {

  private SimpleDateFormat inSDF = new SimpleDateFormat("mm/dd/yyyy");
  private SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

  public String formatDate(String inDate) {
    String outDate = "";
    Format formatter = new SimpleDateFormat( "hh:mm:ss" );
    if (inDate != null) {
        try {
            Date date = inSDF.parse(inDate);
            outDate = outSDF.format(date);
        } catch (ParseException ex) {
            System.out.println("Unable to format date: " + inDate + ex.getMessage());
            ex.printStackTrace();
        }
    }
    return outDate+" "+formatter.format( new Date() );
  }
}
