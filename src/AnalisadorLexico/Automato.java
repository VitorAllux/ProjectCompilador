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
	private HashMap<String, String> tabelaParsing = new HashMap<String, String>(); 

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
	
	public void analiseSintatica(Stack<Token> listA) {
		ArrayList<TokenX> listX = new ArrayList<TokenX>();
		String[] simbolos;
		listX.add(new TokenX(52, "programa"));
		int i = 0;
		String parsing, id;
		TokenX X;
		Token A;
		
		while(!listX.isEmpty()) {
			X = listX.get(i);
			A = listA.get(i);
			if(X.codigo < 52) {
				if(X.getCodigo() == A.getCodigo()) {
					listX.remove(i);
					listA.remove(i);					
				}else {
					erros.add(new Erro("Erro sintatico", "sintatico", listA.get(i).getLinha()));
				}
			}else {
				parsing = convertToParsing(convertToCondenate(X.getCodigo(), A.getCodigo()));
				if(parsing!=null) {
					listX.remove(i);
					if(!parsing.equals("NULL")) {
						simbolos = parsing.split("[|]");
						for(int k = simbolos.length - 1; k >= 0; k--) {
							id = tabelaSimbolos.get(simbolos[k].toUpperCase());
							listX.add(0, new TokenX(Integer.parseInt(id==null?"0":id), simbolos[k]));						
						}
					}
				}else {
					erros.add(new Erro("Erro sintatico", "sintatico", A.getLinha()));
				}
			}
			if(!erros.isEmpty()) {				
				frame.printError(erros);
				frame.newText(erros);
				break;
			}
		}
	}
	
	private String convertToCondenate(int x, int a) {
		return Integer.toString(x)+","+Integer.toString(a);		
	}
	
	private String convertToParsing(String cordenate) {
		return tabelaParsing.get(cordenate);		
	}
		
	
	private void addInStack(String str, int linha, boolean literal) {		
		if(literal) {
			System.out.println("procurou '"+str+"' result 48");
		}
		
		simbols.add(new Token(literal ? 48 : getSymbolID(str), linha, str));		
	}

	public Integer getSymbolID(String symbol) {

		String str = tabelaSimbolos.get(symbol.toUpperCase());
		str = ((str!=null)&&(str.matches("(25|26|48)"))? null: str);
		if(str == null||Integer.parseInt(str)>51) {
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
		
		tabelaSimbolos.put("PROGRAM", "1");
		tabelaSimbolos.put("LABEL", "2");
		tabelaSimbolos.put("CONST", "3");
		tabelaSimbolos.put("VAR", "4");
		tabelaSimbolos.put("PROCEDURE", "5");
		tabelaSimbolos.put("BEGIN", "6");
		tabelaSimbolos.put("END", "7");
		tabelaSimbolos.put("INTEGER", "8");
		tabelaSimbolos.put("ARRAY", "9");
		tabelaSimbolos.put("OF", "10");
		tabelaSimbolos.put("CALL", "11");
		tabelaSimbolos.put("GOTO", "12");
		tabelaSimbolos.put("IF", "13");
		tabelaSimbolos.put("THEN", "14");
		tabelaSimbolos.put("ELSE", "15");
		tabelaSimbolos.put("WHILE", "16");
		tabelaSimbolos.put("DO", "17");
		tabelaSimbolos.put("REPEAT", "18");
		tabelaSimbolos.put("UNTIL", "19");		
		tabelaSimbolos.put("READLN", "20");
		tabelaSimbolos.put("WRITELN", "21");
		tabelaSimbolos.put("OR", "22");
		tabelaSimbolos.put("AND", "23");
		tabelaSimbolos.put("NOT", "24");
		tabelaSimbolos.put("IDENTIFICADOR", "25");
		tabelaSimbolos.put("INTEIRO", "26");
		tabelaSimbolos.put("FOR", "27");
		tabelaSimbolos.put("TO", "28");
		tabelaSimbolos.put("CASE", "29");
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
		tabelaSimbolos.put("LITERAL", "48");
		tabelaSimbolos.put(".", "49");
		tabelaSimbolos.put("..", "50");
		tabelaSimbolos.put("$", "51");
		//nao terminais
		tabelaSimbolos.put("PROGRAMA", "52");
		tabelaSimbolos.put("BLOCO", "53");
		tabelaSimbolos.put("DCLROT" ,"54");
		tabelaSimbolos.put("LID" ,"55");
		tabelaSimbolos.put("REPIDENT" ,"56");
		tabelaSimbolos.put("DCLCONST" ,"57");
		tabelaSimbolos.put("LDCONST" ,"58");
		tabelaSimbolos.put("DCLVAR" ,"59");
		tabelaSimbolos.put("LDVAR" ,"60");
		tabelaSimbolos.put("TIPO" ,"61");
		tabelaSimbolos.put("DCLPROC" ,"62");
		tabelaSimbolos.put("DEFPAR" ,"63");
		tabelaSimbolos.put("CORPO" ,"64");
		tabelaSimbolos.put("REPCOMANDO" ,"65");
		tabelaSimbolos.put("COMANDO" ,"66");
		tabelaSimbolos.put("RCOMID" ,"67");
		tabelaSimbolos.put("RVAR" ,"68");
		tabelaSimbolos.put("PARAMETROS" ,"69");
		tabelaSimbolos.put("REPPAR" ,"70");
		tabelaSimbolos.put("ELSEPARTE" ,"71");
		tabelaSimbolos.put("VARIAVEL" ,"72");
		tabelaSimbolos.put("VARIAVEL1" ,"73");
		tabelaSimbolos.put("REPVARIAVEL" ,"74");
		tabelaSimbolos.put("ITEMSAIDA" ,"75");
		tabelaSimbolos.put("REPITEM" ,"76");
		tabelaSimbolos.put("EXPRESSAO" ,"77");
		tabelaSimbolos.put("REPEXPSIMP" ,"78");
		tabelaSimbolos.put("EXPSIMP" ,"79");
		tabelaSimbolos.put("REPEXP" ,"80");
		tabelaSimbolos.put("TERMO" ,"81");
		tabelaSimbolos.put("REPTERMO" ,"82");
		tabelaSimbolos.put("FATOR" ,"83");
		tabelaSimbolos.put("CONDCASE" ,"84");
		tabelaSimbolos.put("CONTCASE" ,"85");
		tabelaSimbolos.put("RPINTEIRO" ,"86");
		tabelaSimbolos.put("SEMEFEITO" ,"87");
		
		
		//tabela parsing - derivações
		tabelaParsing.put("52,1" ,"PROGRAM|IDENTIFICADOR|;|BLOCO|." );
		tabelaParsing.put("53,2" ,"DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO" );
		tabelaParsing.put("53,3" ,"DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO" );
		tabelaParsing.put("53,4" ,"DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO" );
		tabelaParsing.put("53,5" ,"DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO" );
		tabelaParsing.put("53,6" ,"DCLROT|DCLCONST|DCLVAR|DCLPROC|CORPO" );
		tabelaParsing.put("54,2" ,"LABEL|LID|;" );
		tabelaParsing.put("54,3" ,"NULL" );
		tabelaParsing.put("54,4" ,"NULL" );
		tabelaParsing.put("54,5" ,"NULL" );
		tabelaParsing.put("54,6" ,"NULL" );
		tabelaParsing.put("55,25" ,"IDENTIFICADOR|REPIDENT" );
		tabelaParsing.put("56,39" ,"NULL" );		
		tabelaParsing.put("56,46" ,",|IDENTIFICADOR|REPIDENT" );		
		tabelaParsing.put("56,47" ,"NULL" );
		tabelaParsing.put("57,3" ,"CONST|IDENTIFICADOR|=|INTEIRO|;|LDCONST" );
		tabelaParsing.put("57,4" ,"NULL" );
		tabelaParsing.put("57,5" ,"NULL" );
		tabelaParsing.put("57,6" ,"NULL" );
		tabelaParsing.put("58,4" ,"NULL" );
		tabelaParsing.put("58,5" ,"NULL" );
		tabelaParsing.put("58,6" ,"NULL" );
		tabelaParsing.put("58,25" ,"IDENTIFICADOR|=|INTEIRO|;|LDCONST" );
		tabelaParsing.put("59,4" ,"VAR|LID|:|TIPO|;|LDVAR" );
		tabelaParsing.put("59,5" ,"NULL" );
		tabelaParsing.put("59,6" ,"NULL" );
		tabelaParsing.put("60,5" ,"NULL" );
		tabelaParsing.put("60,6" ,"NULL" );
		tabelaParsing.put("60,25" ,"LID|:|TIPO|;|LDVAR" );
		tabelaParsing.put("61,8" ,"INTEGER" );
		tabelaParsing.put("61,9" ,"ARRAY|[|INTEIRO|..|INTEIRO|]|OF|INTEGER" );
		tabelaParsing.put("62,5" ,"PROCEDURE|IDENTIFICADOR|DEFPAR|;|BLOCO|;|DCLPROC" );
		tabelaParsing.put("62,6" ,"NULL" );
		tabelaParsing.put("63,36" ,"(|LID|:|INTEGER|)" );
		tabelaParsing.put("63,39" ,"NULL" );
		tabelaParsing.put("64,6" ,"BEGIN|COMANDO|REPCOMANDO|END" );
		tabelaParsing.put("65,7" ,"NULL" );
		tabelaParsing.put("65,47" ,";|COMANDO|REPCOMANDO" );
		tabelaParsing.put("66,6" ,"CORPO" );
		tabelaParsing.put("66,7" ,"NULL" );
		tabelaParsing.put("66,11" ,"CALL|IDENTIFICADOR|PARAMETROS" );
		tabelaParsing.put("66,12" ,"GOTO|IDENTIFICADOR" );
		tabelaParsing.put("66,13" ,"IF|EXPRESSAO|THEN|COMANDO|ELSEPARTE" );
		tabelaParsing.put("66,15" ,"NULL" );
		tabelaParsing.put("66,16" ,"WHILE|EXPRESSAO|DO|COMANDO" );
		tabelaParsing.put("66,18" ,"REPEAT|COMANDO|UNTIL|EXPRESSAO" );
		tabelaParsing.put("66,19" ,"NULL" );
		tabelaParsing.put("66,20" ,"READLN|(|VARIAVEL|REPVARIAVEL|)" );
		tabelaParsing.put("66,21" ,"WRITELN|(|ITEMSAIDA|REPITEM|)" );		
		tabelaParsing.put("66,25" ,"IDENTIFICADOR|RCOMID" );
		tabelaParsing.put("66,27" ,"FOR|IDENTIFICADOR|:=|EXPRESSAO|TO|EXPRESSAO|DO|COMANDO" );
		tabelaParsing.put("66,29" ,"CASE|EXPRESSAO|OF|CONDCASE|END" );
		tabelaParsing.put("66,47" ,"NULL" );
		tabelaParsing.put("67,34" ,"RVAR|:=|EXPRESSAO" );
		tabelaParsing.put("67,38" ,"RVAR|:=|EXPRESSAO" );		
		tabelaParsing.put("67,39" ,":|COMANDO" );
		tabelaParsing.put("68,34" ,"[|EXPRESSAO|]" );
		tabelaParsing.put("68,38" ,"NULL" );
		tabelaParsing.put("69,7" ,"NULL" );
		tabelaParsing.put("69,15" ,"NULL" );
		tabelaParsing.put("69,19" ,"NULL" );
		tabelaParsing.put("69,36" ,"(|EXPRESSAO|REPPAR|)" );
		tabelaParsing.put("69,47" ,"NULL" );
		tabelaParsing.put("70,37" ,"NULL" );
		tabelaParsing.put("70,46" ,",|EXPRESSAO|REPPAR" );
		tabelaParsing.put("71,7" ,"NULL" );
		tabelaParsing.put("71,15" ,"ELSE|COMANDO" );
		tabelaParsing.put("71,19" ,"NULL" );
		tabelaParsing.put("71,47" ,"NULL" );
		tabelaParsing.put("72,25" ,"IDENTIFICADOR|VARIAVEL1" );
		tabelaParsing.put("73,7" ,"NULL" );
		tabelaParsing.put("73,10" ,"NULL" );
		tabelaParsing.put("73,14" ,"NULL" );
		tabelaParsing.put("73,15" ,"NULL" );
		tabelaParsing.put("73,17" ,"NULL" );
		tabelaParsing.put("73,19" ,"NULL" );
		tabelaParsing.put("73,22" ,"NULL" );
		tabelaParsing.put("73,23" ,"NULL" );
		tabelaParsing.put("73,28" ,"NULL" );
		tabelaParsing.put("73,30" ,"NULL" );
		tabelaParsing.put("73,31" ,"NULL" );
		tabelaParsing.put("73,32" ,"NULL" );
		tabelaParsing.put("73,33" ,"NULL" );
		tabelaParsing.put("73,34" ,"[|EXPRESSAO|]" );
		tabelaParsing.put("73,35" ,"NULL" );
		tabelaParsing.put("73,37" ,"NULL" );
		tabelaParsing.put("73,40" ,"NULL" );
		tabelaParsing.put("73,41" ,"NULL" );
		tabelaParsing.put("73,42" ,"NULL" );
		tabelaParsing.put("73,43" ,"NULL" );
		tabelaParsing.put("73,44" ,"NULL" );
		tabelaParsing.put("73,45" ,"NULL" );
		tabelaParsing.put("73,46" ,"NULL" );
		tabelaParsing.put("73,47" ,"NULL" );
		tabelaParsing.put("74,37" ,"NULL" );
		tabelaParsing.put("74,46" ,",|VARIAVEL|REPVARIAVEL" );
		tabelaParsing.put("75,24" ,"EXPRESSAO" );
		tabelaParsing.put("75,25" ,"EXPRESSAO" );
		tabelaParsing.put("75,26" ,"EXPRESSAO" );
		tabelaParsing.put("75,30" ,"EXPRESSAO" );
		tabelaParsing.put("75,31" ,"EXPRESSAO" );
		tabelaParsing.put("75,36" ,"EXPRESSAO" );
		tabelaParsing.put("75,48" ,"LITERAL" );
		tabelaParsing.put("76,37" ,"NULL" );
		tabelaParsing.put("76,46" ,",|ITEMSAIDA|REPITEM" );
		tabelaParsing.put("77,24" ,"EXPSIMP|REPEXPSIMP" );
		tabelaParsing.put("77,25" ,"EXPSIMP|REPEXPSIMP" );
		tabelaParsing.put("77,26" ,"EXPSIMP|REPEXPSIMP" );
		tabelaParsing.put("77,30" ,"EXPSIMP|REPEXPSIMP" );
		tabelaParsing.put("77,31" ,"EXPSIMP|REPEXPSIMP" );
		tabelaParsing.put("77,36" ,"EXPSIMP|REPEXPSIMP" );
		tabelaParsing.put("78,7" ,"NULL" );
		tabelaParsing.put("78,10" ,"NULL" );
		tabelaParsing.put("78,14" ,"NULL" );
		tabelaParsing.put("78,15" ,"NULL" );
		tabelaParsing.put("78,17" ,"NULL" );
		tabelaParsing.put("78,19" ,"NULL" );
		tabelaParsing.put("78,28" ,"NULL" );
		tabelaParsing.put("78,35" ,"NULL" );
		tabelaParsing.put("78,37" ,"NULL" );
		tabelaParsing.put("78,40" ,"=|EXPSIMP" );
		tabelaParsing.put("78,41" ,">|EXPSIMP" );
		tabelaParsing.put("78,42" ,">=|EXPSIMP" );
		tabelaParsing.put("78,43" ,"<;|EXPSIMP" );
		tabelaParsing.put("78,44" ,"<=|EXPSIMP" );
		tabelaParsing.put("78,45" ,"<>|EXPSIMP" );
		tabelaParsing.put("78,46" ,"NULL" );
		tabelaParsing.put("78,47" ,"NULL" );
		tabelaParsing.put("79,24" ,"TERMO|REPEXP" );
		tabelaParsing.put("79,25" ,"TERMO|REPEXP" );
		tabelaParsing.put("79,26" ,"TERMO|REPEXP" );
		tabelaParsing.put("79,30" ,"+|TERMO|REPEXP" );
		tabelaParsing.put("79,31" ,"-|TERMO|REPEXP" );
		tabelaParsing.put("79,36" ,"TERMO|REPEXP" );
		tabelaParsing.put("80,7" ,"NULL" );
		tabelaParsing.put("80,10" ,"NULL" );
		tabelaParsing.put("80,14" ,"NULL" );
		tabelaParsing.put("80,15" ,"NULL" );
		tabelaParsing.put("80,17" ,"NULL" );
		tabelaParsing.put("80,19" ,"NULL" );
		tabelaParsing.put("80,22" ,"OR|TERMO|REPEXP" );
		tabelaParsing.put("80,28" ,"NULL" );
		tabelaParsing.put("80,30" ,"+|TERMO|REPEXP" );
		tabelaParsing.put("80,31" ,"-|TERMO|REPEXP" );
		tabelaParsing.put("80,35" ,"NULL" );
		tabelaParsing.put("80,37" ,"NULL" );
		tabelaParsing.put("80,40" ,"NULL" );
		tabelaParsing.put("80,41" ,"NULL" );
		tabelaParsing.put("80,42" ,"NULL" );
		tabelaParsing.put("80,43" ,"NULL" );
		tabelaParsing.put("80,44" ,"NULL" );
		tabelaParsing.put("80,45" ,"NULL" );
		tabelaParsing.put("80,46" ,"NULL" );
		tabelaParsing.put("80,47" ,"NULL" );
		tabelaParsing.put("81,24" ,"FATOR|REPTERMO" );
		tabelaParsing.put("81,25" ,"FATOR|REPTERMO" );
		tabelaParsing.put("81,26" ,"FATOR|REPTERMO" );
		tabelaParsing.put("81,36" ,"FATOR|REPTERMO" );
		tabelaParsing.put("82,7" ,"NULL" );
		tabelaParsing.put("82,10" ,"NULL" );
		tabelaParsing.put("82,14" ,"NULL" );
		tabelaParsing.put("82,15" ,"NULL" );
		tabelaParsing.put("82,17" ,"NULL" );
		tabelaParsing.put("82,19" ,"NULL" );
		tabelaParsing.put("82,22" ,"NULL" );
		tabelaParsing.put("82,23" ,"AND|FATOR|REPTERMO" );
		tabelaParsing.put("82,28" ,"NULL" );
		tabelaParsing.put("82,30" ,"NULL" );
		tabelaParsing.put("82,31" ,"NULL" );
		tabelaParsing.put("82,32" ,"*|FATOR|REPTERMO" );
		tabelaParsing.put("82,33" ,"/|FATOR|REPTERMO" );
		tabelaParsing.put("82,35" ,"NULL" );
		tabelaParsing.put("82,37" ,"NULL" );
		tabelaParsing.put("82,40" ,"NULL" );
		tabelaParsing.put("82,41" ,"NULL" );
		tabelaParsing.put("82,42" ,"NULL" );
		tabelaParsing.put("82,43" ,"NULL" );
		tabelaParsing.put("82,44" ,"NULL" );
		tabelaParsing.put("82,45" ,"NULL" );
		tabelaParsing.put("82,46" ,"NULL" );
		tabelaParsing.put("82,47" ,"NULL" );
		tabelaParsing.put("83,24" ,"NOT|FATOR" );
		tabelaParsing.put("83,25" ,"VARIAVEL" );
		tabelaParsing.put("83,26" ,"INTEIRO" );
		tabelaParsing.put("83,36" ,"(|EXPRESSAO|)" );
		tabelaParsing.put("84,26" ,"INTEIRO|RPINTEIRO|:|COMANDO|CONTCASE" );
		tabelaParsing.put("85,7" ,"NULL" );
		tabelaParsing.put("85,47" ,";|CONDCASE" );
		tabelaParsing.put("86,39" ,"NULL" );
		tabelaParsing.put("86,46" ,",|INTEIRO|RPINTEIRO" );

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

}
