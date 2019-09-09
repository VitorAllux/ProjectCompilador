package AnalisadorLexico;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

@SuppressWarnings("serial")
public class ColorPane extends JTextPane {

	private boolean comment = false, maybeComment = false, maybeEndComment = false;


	public ColorPane() {
		this.setStyledDocument(doc);
	}


	final StyleContext cont = StyleContext.getDefaultStyleContext();
	final javax.swing.text.AttributeSet attrReserved = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
	final javax.swing.text.AttributeSet attrComment = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.lightGray);
	final javax.swing.text.AttributeSet attrNumber = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.MAGENTA);
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
			int auxPos = 0;
			boolean useComment = false;

			while (wordR <= after) {
				if (wordR == after || (String.valueOf(text.charAt(wordR)).matches("\\W")/*&&String.valueOf(text.charAt(wordR)).matches("\\D")*/)) {
					if(wordR<text.length()&&!comment) {						
						if (maybeComment&&String.valueOf(text.charAt(wordR)).equals("*")) {
							comment = true;
							auxPos = wordR-1;
						}	
						maybeComment = String.valueOf(text.charAt(wordR)).equals("(");

					}

					if(!comment) {
						if (text.substring(wordL, wordR).matches("(?i)^((\\W)*(program|label|const|var|procedure|begin|end|integer|array|of|call|goto|if|then|else|while|readln|writeln|or|and|not|for|to|case|while|while|while))")) {
							setCharacterAttributes(wordL, wordR - wordL, attrReserved, false);
						}else {
							//if(23-42) (* teste *)    
							/*
							 * while((wordL<text.length())&&!String.valueOf(text.charAt(wordL)).matches(
							 * "(\\d)|(-)")) { wordL++; }
							 */
							if (text.substring(wordL, wordR).matches("(-)?([0-9])+")) {
								setCharacterAttributes(wordL, wordR - wordL, attrNumber, false);                        		
							}else {
								setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
							}

						}
					}else {
						if(wordR<text.length()) {
							if(maybeEndComment&&String.valueOf(text.charAt(wordR)).equals(")")) {
								comment = false;
								if(useComment) {
									setCharacterAttributes(auxPos, (wordR + 1) - auxPos, attrComment, false);
								}
							}
							maybeEndComment = String.valueOf(text.charAt(wordR)).equals("*");
							if(!comment) {
								wordR++;
							}
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
				setCharacterAttributes(before, after - before, attrReserved, false);
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
		int len = getDocument().getLength(); 
		setCaretPosition(len); 
		setCharacterAttributes(aset, false);

		replaceSelection(text); 
	}


}