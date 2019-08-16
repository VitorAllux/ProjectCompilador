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
				}									
			}
		}	
		
		
		
		return simbols;
		
	}
	
	public Integer getSymbolID(String symbol) {
		
		return Integer.parseInt(tabelaSimbolos.get(symbol));
		
		
	}

}
