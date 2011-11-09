<<<<<<< HEAD
package managgui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 *
 * @author Angelo
 */
public class JustNumber extends PlainDocument {
    
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str != null && str.matches("[\\d]*"))
            super.insertString(offs, str, a);
    }
    
}
=======
package managgui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 *
 * @author Angelo
 */
public class JustNumber extends PlainDocument {
    
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str != null && str.matches("[\\d]*"))
            super.insertString(offs, str, a);
    }
    
}
>>>>>>> 484315bbc8a83593a47b1c66f8fee38723390935
