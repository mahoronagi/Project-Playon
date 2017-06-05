package tool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import tool.*;
 

public class GetIPadd {
    
    public static void main(String[] args) {
 
        InetAddress ip;
        String hostname;
        //InetAddress client_ip;
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            InetAddress client_ip = InetAddress.getLoopbackAddress();
            System.out.println("Your current IP address : " + ip);
            System.out.println("Your current Hostname : " + hostname);
            System.out.println("Your current Client_ip : " + client_ip);
 
        } catch (UnknownHostException e) {
 
            e.printStackTrace();
        }
        
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date).toString());
        
        FormatDate days = new FormatDate();
        System.out.println(days.SumDate(30));
    }
}