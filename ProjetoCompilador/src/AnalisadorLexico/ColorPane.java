package AnalisadorLexico;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ColorPane extends JTextPane {
	
    public void append(Color cor, String text) {

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, cor);
        AttributeSet aset2 = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Italic, true);
        int len = getDocument().getLength(); 
        setCaretPosition(len); 
        setCharacterAttributes(aset, false);
        setCharacterAttributes(aset2, false);
        replaceSelection(text.concat("\n")); 
    }
}
