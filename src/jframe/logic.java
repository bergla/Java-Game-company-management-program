/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframe;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import oru.inf.InfDB;
import oru.inf.InfException;

/**
 *
 * @author AAX
 */
public class logic {
    private InfDB databas;
    private int sid;
    
    public logic() {
        try {
            String path = Paths.get("").toAbsolutePath().toString() + File.separator + "MICEDB.FDB";
            databas = new InfDB(path);
            }
        catch (InfException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    
    public static int getID(JTable table) { // Tar emot aktuell tabell och kollar vilket rad man har valt, sedan så tar den ut IDt i den första kolumnen från vald rad.
        int row=-1;
        int result=-1;
        if(table.getSelectedRow()!=-1)
        row = table.getSelectedRow();
        if(row==-1)
            JOptionPane.showMessageDialog(null, "Markera en rad i tabellen!");
        else 
        result = Integer.parseInt( table.getValueAt(row, 0).toString());
        return result;
    }
    public void addProjleader(int aid) {
        try {
            String projektledare = "insert into projektledare (AID) VALUES ("+aid+")";
            databas.insert(projektledare);
        }catch (NumberFormatException e) {  
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Något gick fel med inmatningen");
            }
            catch (InfException e) {  
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Något gick fel vid kontakten med databasen");
                
            }
        
    }
    
    public void updateProjleader(int aid, int sid) throws NumberFormatException, InfException {
        try {
            String projektledare = "update spelprojekt SET AID="+aid+" where SID="+sid;
            databas.update(projektledare);
        }catch (NumberFormatException e) {  
                System.out.println(e.getMessage());
                throw e;
                //JOptionPane.showMessageDialog(null, "Något gick fel med inmatningen");
            }
            catch (InfException e) {  
                System.out.println(e.getMessage());
                throw e;
            }
    }
    
    public void removeProjleader(int aid) throws NumberFormatException, InfException {
        try {
            String projektledare = "delete from projektledare where AID="+aid;
            databas.delete(projektledare);
        }catch (NumberFormatException e) {  
                System.out.println(e.getMessage());
                throw e;
                //JOptionPane.showMessageDialog(null, "Något gick fel med inmatningen");
            }
            catch (InfException e) {  
                System.out.println(e.getMessage());
                throw e;
                //JOptionPane.showMessageDialog(null, "Något gick fel vid kontakten med databasen");
                
            }
        
    }
    
    public boolean checkProjleader(int aid){ // Returnar true om personen finns som projekledare i projektledare tabellen.
        try {
                ArrayList<String> aidlist = new ArrayList<String>();
                aidlist = databas.fetchColumn("select aid from projektledare");
                String aidstring = Integer.toString(aid);
                //boolean exists = false;
                for(int i=0;i<aidlist.size();i++) {
                    if(aidlist.get(i).equals(aidstring)) {
                        return true;
                    }
                    //if(i==aidlist.size()-1 && exists == false)
                     //   return false;
                }
                return false;
        }catch (NumberFormatException e) {  
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Något gick fel med inmatningen");
            }
            catch (InfException e) {  
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Något gick fel vid kontakten med databasen");
                
            }
            return false;
    }
    
    
}
