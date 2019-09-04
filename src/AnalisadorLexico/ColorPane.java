package AnalisadorLexico;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class ColorPane extends JTextPane {
	
	public ColorPane() {
		this.setStyledDocument(doc);
	}
	
    
    final StyleContext cont = StyleContext.getDefaultStyleContext();
    final javax.swing.text.AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
    final javax.swing.text.AttributeSet attrGreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.MAGENTA);
    final javax.swing.text.AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
    
    //Styled Document
    DefaultStyledDocument doc = new DefaultStyledDocument() {
        public void insertString (int offset, String str, javax.swing.text.AttributeSet a) throws BadLocationException {
            super.insertString(offset, str, a);

            String text = getText(0, getLength());
            int before = findLastNonWordChar(text, offset);
            if (before < 0) before = 0;
            int after = findFirstNonWordChar(text, offset + str.length());
            int wordL = before;
            int wordR = before;

            while (wordR <= after) {
                if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                    if (text.substring(wordL, wordR).matches("(\\W)*(begin|end|for|if)")) {
                        setCharacterAttributes(wordL, wordR - wordL, attr, false);
                    }else {
                    	if (text.substring(wordL, wordR).matches("(\\D)*(-)?([0-9])+")) {
                            setCharacterAttributes(wordL, wordR - wordL, attrGreen, false);                        		
                    	} else {
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        }

                    }
                    
                    wordL = wordR;
                }
                wordR++;
            }
        }

        public void remove (int offs, int len) throws BadLocationException {
            super.remove(offs, len);

            String text = getText(0, getLength());
            int before = findLastNonWordChar(text, offs);
            if (before < 0) before = 0;
            int after = findFirstNonWordChar(text, offs);

            if (text.substring(before, after).matches("(\\W)*(begin|end|for|if)")) {
                setCharacterAttributes(before, after - before, attr, false);
            } else {
                setCharacterAttributes(before, after - before, attrBlack, false);
            }
        }
    };
    
	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	
	// Função de append, "concatenar string no textpane"
    public void append(Color cor, String text, Boolean quebraLinha) {

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,StyleConstants.Foreground, cor);
        AttributeSet aset2 = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Italic, true);
        int len = getDocument().getLength(); 
        setCaretPosition(len); 
        setCharacterAttributes(aset, false);
        setCharacterAttributes(aset2, false);

        replaceSelection(text); 
    }

    
}
