/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframe;

import javax.swing.JTable;

/**
 *
 * @author AAX
 */
public class tablehandler {
    void cleartable(JTable table) {
        for (int i = 0; i < table.getRowCount(); i++)
      for(int j = 0; j < table.getColumnCount(); j++) {
          table.setValueAt("", i, j);
      }
    }
}



