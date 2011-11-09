package managgui;

import javax.swing.*;
import javax.swing.JDialog;
/**
 *
 * @author Angelo
 */
class FinestraSwingJD extends JFrame {

    FinestraSwingJD(String s, int x_Loc, int y_Loc, int x_Siz, int y_Siz, JDialog pann, JButton button) {
        super(s);
        getRootPane().setDefaultButton(button);
        setLocation(x_Loc, y_Loc);
        setSize(x_Siz, y_Siz);
//        getContentPane().add(pann);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

        
}
