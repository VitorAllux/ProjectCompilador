package AnalisadorLexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Automatos {
	
	private Stack<Token> simbols = new Stack<Token>();	

	private HashMap<String, String> tabelaSimbolos = new HashMap<String, String>(); 

	public boolean nativeSymbols(Character s) {

		return " 	;,.+-/*()[]".indexOf(s) != -1;

	}

	public boolean nativeSequence(Character s) {

		return ":=<>".indexOf(s) != -1;

	}

	public Stack<Token> splitSimbols(ArrayList<String> list){

		String simbolo = "", combinado = "";
		Character s;
		int linha = 0;
		boolean isLiteral = false, ignore = false;//, isLastIdentifier = false;

		//"if (20 >= 10)<>(10 = 10) then begin"
		for(String str: list) {
			simbolo = "";
			combinado = "";
			linha++;
			for(int i = 0; i < str.length(); i++) {
				s = str.charAt(i); 
				
				//Trata comentario
				if(s=='('&&str.charAt(i+1)=='*') {
					ignore = true;
				}
				if(ignore&&(s=='*'&&str.charAt(i+1)==')')) {
					i++;
					ignore = false;
					continue;
				}
				if(ignore) {
					continue;				
				}
				
					
				                                          //nagativo seguido de digito    
				if (!nativeSymbols(s)||isLiteral||((s == '-')&&(Character.isDigit(str.charAt(i+1))))) {
					if(s == 39) {
						isLiteral = !isLiteral;
						if(!simbolo.isEmpty()) {
							if(!isLiteral) {
								simbolo = simbolo + Character.toString((char)39);
							}
							addInStack(simbolo, linha, !isLiteral);
							simbolo = "";
							if(!isLiteral) {
								continue;							
							}
						}					
					}
					
					if(nativeSequence(s)&&!isLiteral) {
						combinado = combinado + s;
						if(nativeSequence(str.charAt(i+1))) {
							combinado = combinado + str.charAt(i+1);
							i++;
						}
						if(!simbolo.trim().isEmpty()) {
							addInStack(simbolo, linha, false);
							simbolo = "";
						}
						addInStack(combinado, linha, false);
						combinado = "";					
					}else {
						simbolo = simbolo + s;
						if(i+1 >= str.length()) {
							addInStack(simbolo, linha, false);
						}
					}
				}else {
					if(!simbolo.trim().isEmpty()) {
						addInStack(simbolo, linha, false);
					}
					if((!Character.isWhitespace(s))&&!(s.toString().indexOf("\t")>-1)) {
						addInStack(s.toString(), linha, false);					
					}
						
						simbolo = "";
						combinado = "";
				}
			}
		}

		return simbols;

	}
	
	private void addInStack(String str, int linha, boolean literal) {
		
		if(literal) {
			System.out.println("procurou '"+str+"' result 48");
		}
		
		simbols.add(new Token(literal ? 48 : getSymbolID(str), linha, str));
		
	}

	public Integer getSymbolID(String symbol) {

		String str = tabelaSimbolos.get(symbol.toLowerCase());
		if(str == null) {
			str = (symbol.matches("-?\\d+")) ? "26" : "25"; 			
		}
		System.out.println("procurou '"+symbol+"' result " + str);

		return Integer.parseInt(str);

	}

	public Automatos() {

		tabelaSimbolos.put("program", "1");
		tabelaSimbolos.put("label", "2");
		tabelaSimbolos.put("const", "3");
		tabelaSimbolos.put("var", "4");
		tabelaSimbolos.put("procedure", "5");
		tabelaSimbolos.put("integer", "8");
		tabelaSimbolos.put("if", "13");
		tabelaSimbolos.put("+", "30");
		tabelaSimbolos.put("readln", "20");
		tabelaSimbolos.put("call", "11");
		tabelaSimbolos.put("begin", "6");
		tabelaSimbolos.put("end", "7");
		tabelaSimbolos.put("else", "15");
		tabelaSimbolos.put("then", "14");
		tabelaSimbolos.put("(", "36");
		tabelaSimbolos.put(")", "37");
		tabelaSimbolos.put(":=", "38");
		tabelaSimbolos.put(":", "39");
		tabelaSimbolos.put("=", "40");
		tabelaSimbolos.put(">", "41");
		tabelaSimbolos.put(">=", "42");
		tabelaSimbolos.put("<", "43");
		tabelaSimbolos.put("<=", "44");
		tabelaSimbolos.put("<>", "45");
		tabelaSimbolos.put(",", "46");
		tabelaSimbolos.put(";", "47");
		tabelaSimbolos.put("or", "22");
		tabelaSimbolos.put("and", "23");
		tabelaSimbolos.put("not", "24");
		tabelaSimbolos.put("-", "31");

	}

}
