package managgui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 *
 * @author Angelo
 */
public class Prize extends PlainDocument {
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str!=null && str.matches("[\\d]*") || str.matches("."))
            super.insertString(offs, str, a);
    }
}

