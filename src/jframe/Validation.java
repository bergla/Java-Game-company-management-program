package jframe;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import oru.inf.InfDB;
import oru.inf.InfException;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import javax.swing.table.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class Validation {
    
    private InfDB databas;
    
    public Validation() {
   
        try {
            String path = Paths.get("").toAbsolutePath().toString() + File.separator + "MICEDB.FDB";
            databas = new InfDB(path);
            System.out.println("Anslutningen lyckades");
        } catch (InfException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public static boolean emptyString(String q) {
        if(q == null || q.isEmpty()) {
            return true; 
        }
        else {
            return false; 
        }
    }
    
    
    
//    public static boolean dateValid(String text){
//        
//        if (text == null || text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
//        {
//            return false;
//        }
//        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
//        dateformat.setLenient(false);
//        
//        try {
//        dateformat.parse(text);
//        return true;
//        
//        } catch (ParseException ex) {
//        return false;
//    }
//    }
    
    public boolean DateValid(String text) {
    if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
        return false;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    df.setLenient(false);
    try {
        df.parse(text);
        return true;
    } catch (ParseException ex) {
        return false;
    }
}
    
    public boolean CheckAID(String text) {
        
        String sql = "select AID from Anstalld"; 
        Boolean notFound = true; 
        
        try {
        
        if (databas.fetchColumn(sql) != null) {
        ArrayList<String> checkAID = databas.fetchColumn(sql);
                {
                for (int i = 0; i < checkAID.size(); i++) {
                    if (checkAID.get(i).equals(text)) {
                        notFound = false;
                        
                        break;
                    }
                }
                }
                }
            
        }
        catch (InfException ex) 
           {
               System.out.println(ex.getMessage());
                        
            }
    
        return notFound;
    }
    
    public boolean CheckSID(String text) {
        
        String sql = "select SID from Spelprojekt"; 
        Boolean notFound = true; 
        
        try {
        
        if (databas.fetchColumn(sql) != null) {
        ArrayList<String> checkSID = databas.fetchColumn(sql);
                {
                for (int i = 0; i < checkSID.size(); i++) {
                    if (checkSID.get(i).equals(text)) {
                        notFound = false;
                        
                        break;
                    }
                }
                }
                }
            
        }
        catch (InfException ex) 
           {
               System.out.println(ex.getMessage());
                        
            }
    
        return notFound;
    }
    
    public boolean checkInt(String text) {
        
        int x = 0;
        boolean valid = false; 
        try {
        x = Integer.parseInt(text);
        valid = true; 
        
        } catch (NumberFormatException e) {
             System.out.println("Not a number");
        }
        
        return valid;
    }
}
