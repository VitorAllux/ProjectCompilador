package AnalisadorLexico;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Automatos {
	
	private Stack<Pilha> simbols = new Stack<Pilha>();
	

	private HashMap<String, String> tabelaSimbolos = new HashMap<String, String>(); 

	public boolean nativeSymbols(Character s) {

		return " ;,.+-/*:()[]".indexOf(s) != -1;

	}

	public boolean nativeSequence(Character s) {

		return "=<>".indexOf(s) != -1;

	}


	//if codigo23+1 then



	public Stack<Pilha> splitSimbols(String str, int linha){

		String simbolo = "";
		Character s;
		String combinado = "";
		//boolean isLastNative = false, isLastNativeSequence = false, isLastIdentifier = false;

		//"if (20 >= 10)<>(10 = 10) then begin"
		for(int i = 0; i < str.length(); i++) {
			s = str.charAt(i); 
			if (!nativeSymbols(s) /* || (Character.isDigit(s) && !isLastNative) ) && !Character.isWhitespace(s)*/) {
				if(nativeSequence(s)) {
					combinado = combinado + s;
					if(nativeSequence(str.charAt(i+1))) {
						combinado = combinado + str.charAt(i+1);
						i++;
					}
					if(!simbolo.trim().isEmpty()) {
						addInStack(simbolo, linha);
						simbolo = "";
					}
					addInStack(combinado, linha);
					combinado = "";					
				}else {
					simbolo = simbolo + s;
					if(i+1 >= str.length()) {
						addInStack(simbolo, linha);
					}
				}
			}else {
				if(!simbolo.trim().isEmpty()) {
					addInStack(simbolo, linha);
				}
				if(!Character.isWhitespace(s)) {
					addInStack(s.toString(), linha);					
				}
					
					simbolo = "";
					combinado = "";
			}
		}	



		return simbols;

	}
	
	private void addInStack(String str, int linha) {
		
		simbols.add(new Pilha(getSymbolID(str), linha, str));
		
	}

	public Integer getSymbolID(String symbol) {

		String str = tabelaSimbolos.get(symbol); 	
		System.out.println("procurou "+symbol+" result " + str);

		return ((str == null)) ? 25 : Integer.parseInt(str);


	}

	public Automatos() {

		tabelaSimbolos.put("program", "1");
		tabelaSimbolos.put("label", "2");
		tabelaSimbolos.put("const", "3");
		tabelaSimbolos.put("if", "13");
		tabelaSimbolos.put("+", "30");
		tabelaSimbolos.put("begin", "6");
		tabelaSimbolos.put("end", "7");
		tabelaSimbolos.put("else", "15");
		tabelaSimbolos.put("then", "14");
		tabelaSimbolos.put("(", "36");
		tabelaSimbolos.put(")", "37");
		tabelaSimbolos.put(":=", "38");
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

	}

}
