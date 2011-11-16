package managgui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
*
* @author Angelo
*/

public class MyTableCellRenderer extends DefaultTableCellRenderer{
      
    @Override
    public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus, int row, int column){
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        setHorizontalAlignment( CENTER );
         
        return this;
    }
}
