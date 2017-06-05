package tool;

import java.text.*;
import java.time.*;
import java.util.*;

public class FormatDate {

    private SimpleDateFormat inSDF = new SimpleDateFormat("mm/dd/yyyy");
    private SimpleDateFormat outSDF = new SimpleDateFormat("yyyy-mm-dd");

    public String formatDate(String inDate) {
        String outDate = "";
        Format formatter = new SimpleDateFormat("hh:mm:ss");
        if (inDate != null) {
            try {
                Date date = inSDF.parse(inDate);
                outDate = outSDF.format(date);
            } catch (ParseException ex) {
                System.out.println("Unable to format date: " + inDate + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return outDate + " " + formatter.format(new Date());
    }

    public String CurrentDate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(date).toString();
    }
    
    public String SumDate(int day) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, day);
        return dateFormat.format(c.getTime());
    }
}
