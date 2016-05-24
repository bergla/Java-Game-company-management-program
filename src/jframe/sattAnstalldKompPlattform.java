/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframe;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import oru.inf.InfDB;
import oru.inf.InfException;

/**
 *
 * @author AAX
 */
public class sattAnstalldKompPlattform extends javax.swing.JFrame {
    private InfDB databas;
    int aid;
    /**
     * Creates new form sattAnstalldKompPlattform
     */
    public sattAnstalldKompPlattform() {
        initComponents();
        
        try {
            String path = Paths.get("").toAbsolutePath().toString() + File.separator + "MICEDB.FDB";
            databas = new InfDB(path);
            }
        catch (InfException ex) {
            System.out.println(ex.getMessage());
        }
        
        kompdd.removeAllItems();
        platdd.removeAllItems();
    }
    
    public void getValues(int aid) {
        this.aid = aid;
        String sql ="select BENAMNING from kompetensdoman";
        String sql2 ="select BENAMNING from plattform";
        String sql3="select PLATTFORM.BENAMNING AS BENAMNING2, KOMPETENSDOMAN.BENAMNING, HAR_KOMPETENS.KOMPETENSNIVA from "
                + "PLATTFORM, KOMPETENSDOMAN, HAR_KOMPETENS where PLATTFORM.PID = HAR_KOMPETENS.PID and KOMPETENSDOMAN.KID = HAR_KOMPETENS.KID and HAR_KOMPETENS.AID="+aid;
        
        //Populera kompetens tabellen/sidan.
        try {
            ArrayList<HashMap<String, String>> kompetens = databas.fetchRows(sql3); //Tabellen.
            if(kompetens !=null) {
            for (int i = 0; i < kompetens.size(); i++) {
                int col=0;
                kompTable.getModel().setValueAt(kompetens.get(i).get("BENAMNING"), i, col); 
                col++;
                kompTable.getModel().setValueAt(kompetens.get(i).get("BENAMNING2"), i, col);
                col++;
                kompTable.getModel().setValueAt(kompetens.get(i).get("KOMPETENSNIVA"), i, col);
            }
            }
            ArrayList<HashMap<String, String>> kompdoman = databas.fetchRows(sql);
            ArrayList<HashMap<String, String>> plattform = databas.fetchRows(sql2);
            
            kompdd.removeAllItems();
            platdd.removeAllItems();
            
            for (int i = 0; i < kompdoman.size(); i++) {
                kompdd.addItem(kompdoman.get(i).get("BENAMNING")); 
            }
            for (int i = 0; i < plattform.size(); i++) {
                platdd.addItem(plattform.get(i).get("BENAMNING")); 
            }
            
        } catch (InfException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public String getName(int aid) {
        String sql ="SELECT NAMN from anstalld where anstalld.aid="+aid;
        String name="";
        try {
        name = databas.fetchSingle(sql);
        }catch (InfException ex) {
            System.out.println(ex.getMessage());
        }
        return name;
    }
    
    public void setLabel(String name) {
        komplabel.setText("Sätt kompetens för "+name);
    }
    
    public void delKomp(int aid, String komp, String plattform, String level ) {
        String sql1 = "SELECT HAR_KOMPETENS.AID, HAR_KOMPETENS.kid, HAR_KOMPETENS.pid, HAR_KOMPETENS.KOMPETENSNIVA from HAR_KOMPETENS, PLATTFORM, KOMPETENSDOMAN, ANSTALLD"
                + " where HAR_KOMPETENS.PID=PLATTFORM.PID and"
                + " HAR_KOMPETENS.KID=KOMPETENSDOMAN.KID and HAR_KOMPETENS.AID=ANSTALLD.AID and"
                + " ANSTALLD.AID="+aid+" and KOMPETENSDOMAN.BENAMNING='"+komp+"' and PLATTFORM.BENAMNING='"+plattform+"' and HAR_KOMPETENS.KOMPETENSNIVA="+level;
        
        try {
        HashMap<String, String> kompetens = databas.fetchRow(sql1);
        
        String tempaid = kompetens.get("AID");
        String tempkomp = kompetens.get("KID");
        String tempplat = kompetens.get("PID");
        String templevel = kompetens.get("KOMPETENSNIVA");
        String sql = "DELETE FROM HAR_KOMPETENS WHERE AID="+tempaid+" and KID="+tempkomp+" and PID="+tempplat+" and KOMPETENSNIVA="+templevel;
        databas.delete(sql);
        }catch (NumberFormatException e) {  
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Något gick fel med inmatningen");
            }
            catch (InfException e) {  
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Något gick fel vid kontakten med databasen");
                
            }
    }
    
    public void addKomp(String komp, String plattform, String level) {
        
        String sql1 = "SELECT HAR_KOMPETENS.AID, HAR_KOMPETENS.kid, HAR_KOMPETENS.pid, HAR_KOMPETENS.KOMPETENSNIVA from HAR_KOMPETENS, PLATTFORM, KOMPETENSDOMAN, ANSTALLD"
                + " where HAR_KOMPETENS.PID=PLATTFORM.PID and"
                + " HAR_KOMPETENS.KID=KOMPETENSDOMAN.KID and HAR_KOMPETENS.AID=ANSTALLD.AID and"
                + " ANSTALLD.AID="+aid+" and KOMPETENSDOMAN.BENAMNING='"+komp+"' and PLATTFORM.BENAMNING='"+plattform+"' and HAR_KOMPETENS.KOMPETENSNIVA="+level;
        
        String sqlkomp = "SELECT KID FROM KOMPETENSDOMAN WHERE BENAMNING='"+komp+"'";
        String sqlplat = "SELECT PID FROM PLATTFORM WHERE BENAMNING='"+plattform+"'";
        try {
        String tempkomp = databas.fetchSingle(sqlkomp);
        String tempplat = databas.fetchSingle(sqlplat);
        String sql = "insert into HAR_KOMPETENS (AID,KID,PID,KOMPETENSNIVA) values ("+aid+","+tempkomp+","+tempplat+","+level+")";
        databas.insert(sql);
        }catch (NumberFormatException e) {  
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Något gick fel med inmatningen");
            }
            catch (InfException e) {  
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Du måste ange en siffra mellan 1-5");
                
            }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        komplvl = new javax.swing.JTextField();
        kompdd = new javax.swing.JComboBox<>();
        platdd = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        addcomp = new javax.swing.JButton();
        komplabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        kompTable = new javax.swing.JTable();
        delcomp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Kompetensnivå");

        kompdd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        kompdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kompddActionPerformed(evt);
            }
        });

        platdd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Kompetens");

        jLabel4.setText("Plattform");

        addcomp.setText("Lägg till kompetens");
        addcomp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addcompActionPerformed(evt);
            }
        });

        komplabel.setText("Sätt kompentens för");

        kompTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kompetens", "Plattform", "Kompetensnivå"
            }
        ));
        jScrollPane1.setViewportView(kompTable);

        delcomp.setText("Ta bort markerad kompetens");
        delcomp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delcompActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kompdd, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(komplvl, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(platdd, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(addcomp))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(komplabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(delcomp)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(komplabel)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(komplvl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(kompdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(platdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addcomp)
                    .addComponent(delcomp))
                .addContainerGap(79, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kompddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kompddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kompddActionPerformed

    private void addcompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addcompActionPerformed
        // TODO add your handling code here:
        
            String a = kompdd.getSelectedItem().toString();
            String b = platdd.getSelectedItem().toString();
            String c = komplvl.getText();
            addKomp(a,b,c);
            getValues(aid);
        
    }//GEN-LAST:event_addcompActionPerformed

    private void delcompActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delcompActionPerformed
        // TODO add your handling code here:
        if (kompTable.getSelectedRow()==-1) {
            JOptionPane.showMessageDialog(null, "Markera en plattform!");
        } else {
            int row = kompTable.getSelectedRow();
            String a = kompTable.getValueAt(row,0).toString();
            String b = kompTable.getValueAt(row,1).toString();
            String c = kompTable.getValueAt(row,2).toString();
            
            delKomp(aid, a, b, c);
            getValues(aid);
        }
    }//GEN-LAST:event_delcompActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(sattAnstalldKompPlattform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(sattAnstalldKompPlattform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(sattAnstalldKompPlattform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(sattAnstalldKompPlattform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sattAnstalldKompPlattform().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addcomp;
    private javax.swing.JButton delcomp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable kompTable;
    private javax.swing.JComboBox<String> kompdd;
    private javax.swing.JLabel komplabel;
    private javax.swing.JTextField komplvl;
    private javax.swing.JComboBox<String> platdd;
    // End of variables declaration//GEN-END:variables
}
