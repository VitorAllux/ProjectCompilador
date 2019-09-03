package AnalisadorLexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Automato {
	
	private Stack<Token> simbols = new Stack<Token>();
	private ArrayList<Erro> erros = new ArrayList<Erro>();
	private int linha;
	private Menu frame;	
	
	private HashMap<String, String> tabelaSimbolos = new HashMap<String, String>(); 

	public boolean nativeSymbols(Character s) {
		return " 	;,+-/*()[]$".indexOf(s) != -1;
	}

	public boolean nativeSequence(Character s) {
		return ".:=<>".indexOf(s) != -1;
	}
	
	public boolean isLastPos(int pos, String str) {
		return pos + 1 >= str.length();
	}
	
	private boolean isEmptyChar(Character c){
		return c.toString().trim().isEmpty();		
	}

	public Stack<Token> splitSimbols(ArrayList<String> list){

		String simbolo = "", combinado = "";
		Character s;
		int linha = 0;
		boolean isLiteral = false, ignore = false;
		
		//percorre todas linhas
		for(String str: list) {
			simbolo = "";
			combinado = "";
			linha++;
			this.linha = linha;
			//percorre todos caracteres
			for(int i = 0; i < str.length(); i++) {
				s = str.charAt(i); 
				
				//Comentario e Literais não finalizados - Erro
				if(isLastPos(i, str)&&(isLiteral||(ignore&&list.size()==linha))) {
					erros.add(new Erro((isLiteral ? "literal" : "cometario") + " não finalizado", "interno", linha));
				}
				
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
					//Inicia e fecha literais
					if(s == 39) {
						isLiteral = !isLiteral;
						if(!simbolo.isEmpty()) {
							if(!isLiteral) {
								simbolo += Character.toString((char)39);
							}
							addInStack(simbolo, linha, !isLiteral);
							simbolo = "";
							if(!isLiteral) {
								continue;							
							}
						}					
					}
					
					if(nativeSequence(s)&&!isLiteral) {
						//Trata combinações de simbolos ex(>=, .., <>, :=)
						combinado += s;
						if(!isLastPos(i, str)) {
							if(s == 46) {
								if(str.charAt(i+1) == 46) {
									combinado += str.charAt(i+1);
									i++;
								}
							}else {
								if(s == 58) {
									if(str.charAt(i+1) == 61) {
										combinado += str.charAt(i+1);
										i++;
									}
								}else {							
									if(nativeSequence(str.charAt(i+1))) {
										combinado += str.charAt(i+1);
										i++;
									}
								}
							}
						}
						//Finaliza simbolos pendentes e Simbolos combinados feitos
						if(!simbolo.trim().isEmpty()) {
							addInStack(simbolo, linha, false);
						}
						simbolo = "";
						addInStack(combinado, linha, false);
						combinado = "";					
					}else {
						
						//Acumula literal ou simbolos e Força o fechamento do ultimo simbolo da linha
						if(isLiteral||!isEmptyChar(s)) {
							simbolo += s;
						}
						if(isLastPos(i, str)&&!simbolo.trim().isEmpty()) {
							addInStack(simbolo, linha, false);
						}
					}
				}else {
					//Finaliza simbolos pendentes
					if(!simbolo.trim().isEmpty()) {
						addInStack(simbolo, linha, false);
					}
					simbolo = "";				
					//Finaliza o caracter especial sem combinação
					if(!isEmptyChar(s)) {
						addInStack(s.toString(), linha, false);					
					}
					combinado = "";
				}
				if(!erros.isEmpty()) {
					break;
				}
			}
			if(!erros.isEmpty()) {
				break;
			}
		}

		if(!erros.isEmpty()) {
			frame.printError(erros);
			frame.newText(erros);
			return null;
		}else {
			return simbols;
		}		
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
		
		if(str == "26") {
			int valor = Integer.parseInt(symbol);
			if((valor>32767)||(valor<-32767)) {
				erros.add(new Erro("Numero fora de escala", "interno", linha));
			}
		} else {
			if(str == "25") {
				if(!Character.isAlphabetic(symbol.charAt(0))) {
					erros.add(new Erro("Indentificador com inicio invalido: "+symbol.charAt(0), "interno", linha));
				}
			}
		}
		return Integer.parseInt(str);
	}

	public Automato(Menu menu) {

		frame = menu;
		
		tabelaSimbolos.put("program", "1");
		tabelaSimbolos.put("label", "2");
		tabelaSimbolos.put("const", "3");
		tabelaSimbolos.put("var", "4");
		tabelaSimbolos.put("procedure", "5");
		tabelaSimbolos.put("begin", "6");
		tabelaSimbolos.put("end", "7");
		tabelaSimbolos.put("integer", "8");
		tabelaSimbolos.put("array", "9");
		tabelaSimbolos.put("of", "10");
		tabelaSimbolos.put("call", "11");
		tabelaSimbolos.put("goto", "12");
		tabelaSimbolos.put("if", "13");
		tabelaSimbolos.put("then", "14");
		tabelaSimbolos.put("else", "15");
		tabelaSimbolos.put("while", "16");
		tabelaSimbolos.put("readln", "20");
		tabelaSimbolos.put("writeln", "21");
		tabelaSimbolos.put("or", "22");
		tabelaSimbolos.put("and", "23");
		tabelaSimbolos.put("not", "24");
		//25 - Identificador (tratado posteriormente)
		//26 - Inteiro (tratado posteriormente)
		tabelaSimbolos.put("for", "27");
		tabelaSimbolos.put("to", "28");
		tabelaSimbolos.put("case", "29");
		tabelaSimbolos.put("+", "30");
		tabelaSimbolos.put("-", "31");
		tabelaSimbolos.put("*", "32");
		tabelaSimbolos.put("/", "33");
		tabelaSimbolos.put("[", "34");
		tabelaSimbolos.put("]", "35");
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
		//48 - literal (tratado posteriormente)
		tabelaSimbolos.put(".", "49");
		tabelaSimbolos.put("..", "50");
		tabelaSimbolos.put("$", "51");
		
	}

}
