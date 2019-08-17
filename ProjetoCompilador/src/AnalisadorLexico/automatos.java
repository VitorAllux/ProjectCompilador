package AnalisadorLexico;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class automatos {
	
	private HashMap<String, String> tabelaSimbolos = new HashMap<String, String>(); 
	
	public boolean nativeSymbols(Character s) {
			
		return ";,+-/*:()[]0123456789".indexOf(s) != -1;
		
	}
	
	public boolean nativeSequence(Character s) {
		
		return "=<>".indexOf(s) != -1;
		
	}
	
	public automatos() {
		
		tabelaSimbolos.put("program", "1");
		tabelaSimbolos.put("label", "2");
		tabelaSimbolos.put("const", "3");
		tabelaSimbolos.put("if", "13");
		tabelaSimbolos.put("+", "30");
		tabelaSimbolos.put("begin", "6");
		tabelaSimbolos.put("end", "7");
		tabelaSimbolos.put("else", "15");
		tabelaSimbolos.put("then", "14");
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
	
	//if codigo23+1 then
	
	
	
	public Stack<pilha> splitSimbols(String str, int linha){
		
		pilha simbolo = new pilha(0, 0, "");
		Stack<pilha> simbols = new Stack<pilha>();
		Character s;
		boolean isLastNative = false;
		
		for(int i = 0; i < str.length(); i++) {
			s = str.charAt(i); 
			if((!nativeSymbols(s) || (Character.isDigit(s) && !isLastNative)) && !Character.isWhitespace(s)) {
				isLastNative = false;
				simbolo.setSimbolo(simbolo.getSimbolo()+s);				
			}else {
				isLastNative = true;
				if(!simbolo.getSimbolo().trim().isEmpty()) {
					simbolo.setLinha(linha);
					simbolo.setCodigo(getSymbolID(simbolo.getSimbolo()));
					
					simbols.add(simbolo);
					simbolo = new pilha(0,  0,  "");
				}									
			}
		}	
		
		
		
		return simbols;
		
	}
	
	public Integer getSymbolID(String symbol) {
		
		String str = tabelaSimbolos.get(symbol); 	
		System.out.println("procurou "+symbol+" result " + str);
		
		return (str == "") ? 0 : Integer.parseInt(str);
		
		
	}

}
