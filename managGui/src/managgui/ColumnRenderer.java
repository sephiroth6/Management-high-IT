/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managgui;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Claudio
 */
class ColumnRenderer extends JComboBox implements TableCellRenderer  
    {  
          
    @Override
        public void updateUI()  
        {  
            super.updateUI();  
        }  
    @Override
        public void revalidate() {}  
        public Component getTableCellRendererComponent(  
                     JTable table, Object value,  
                     boolean isSelected, boolean hasFocus,  
                     int row, int column)  
        {  
            if (value != null) {  
                //System.out.println(value.toString());  
                removeAllItems();  
                addItem(value);  
            }  
            return this;  
        }  
    }  