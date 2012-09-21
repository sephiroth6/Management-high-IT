package managgui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
/**
 *
 * @author Angelo
 */
public class Price extends PlainDocument {
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str != null && (str.matches("[0-9]*\\.?[0-9]+") || str.matches("[.]")))
            super.insertString(offs, str, a);
    }
}

