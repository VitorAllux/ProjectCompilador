package Compilador;

import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Automato auto = new Automato(null);
		
		Menu menu = new Menu();
		menu.setVisible(true);
		ArrayList<String> list = new ArrayList<String>();
		menu.editor.setText("(*TESTE - Exemplo Válido*)\n" + 
				"PROGRAM TESTE123;\n" + 
				" 'literal teste'	LABEL\n" + 
				"		label_a, label_b; \n" + 
				"\n" + 
				"	CONST\n" + 
				"		a = -100;\n" + 
				"		b = -200;\n" + 
				"	VAR\n" + 
				"		 X, Y, Z : INTEGER;\n" + 
				"		array_a : ARRAY[0..20] OF INTEGER;\n" + 
				"		array_b, array_c, array_d : ARRAY[0..1000] OF INTEGER;\n" + 
				"	\n" + 
				"	(*Declaração de procedure, inicia novo bloco*)\n" + 
				"	PROCEDURE p_teste(idd : INTEGER);\n" + 
				"		LABEL\n" + 
				"			label_a, label_b;\n" + 
				"			\n" + 
				"		CONST\n" + 
				"			a = 100;\n" + 
				"			b = 200;\n" + 
				"\n" + 
				"		VAR \n" + 
				"			X, Y, Z : INTEGER;\n" + 
				"			array_a : ARRAY[0..20] OF INTEGER;\n" + 
				"			array_b : ARRAY[0..1000] OF INTEGER;\n" + 
				"	BEGIN\n" + 
				"		X := a * b;\n" + 
				"\n" + 
				"	END;\n" + 
				"\n" + 
				"(*	\n" + 
				"	- Início do bloco principal\n" + 
				"*)\n" + 
				"\n" + 
				"BEGIN\n" + 
				"	x := 150;\n" + 
				"	begin\n" + 
				"		x := 20;\n" + 
				"	end;\n" + 
				"	\n" + 
				"	(*Chama a procedure*)\n" + 
				"	CALL p_teste( 100 );\n" + 
				"	\n" + 
				"	(*Testa as possibilidades do IF*)\n" + 
				"	IF (10 > 15) THEN\n" + 
				"		BEGIN\n" + 
				"		END\n" + 
				"	ELSE\n" + 
				"		BEGIN\n" + 
				"		END;\n" + 
				"\n" + 
				"	(*Testa as possibilidades do WHILE*)\n" + 
				"	WHILE(x <> 0)DO\n" + 
				"	BEGIN \n" + 
				"	END; \n" + 
				"\n" + 
				"	(*Testa as possibilidades do REPEAT*)\n" + 
				"	REPEAT \n" + 
				"	BEGIN \n" + 
				"	END\n" + 
				"	UNTIL X > 10;\n" + 
				"\n" + 
				"	(*Testa as possibilidades do WRITELN*)\n" + 
				"	WRITELN(X,Z,Y);\n" + 
				"\n" + 
				"	(*Testa as possibilidades do FOR*)\n" + 
				"	FOR x := y > 2 TO 10 - 2 DO\n" + 
				"	BEGIN\n" + 
				"	END;\n" + 
				"\n" + 
				"	(*Testa as possibilidades do CASE*)\n" + 
				"	CASE A + 2 OF\n" + 
				"		10 : BEGIN END;\n" + 
				"		20 : BEGIN END;\n" + 
				"		30 : BEGIN END\n" + 
				"	END;\n" + 
				"END.\n" + 
				"");
		list=menu.getTextArea();
		int k = 1, Countlinha = 1;
		String linha = "";
		
		for(Token item :auto.splitSimbols(list)) {
			if(item.getLinha() != Countlinha) {
				for(k = Countlinha + 1; k < item.getLinha(); k++ ) {
					System.out.println("");
				}
				Countlinha = k;
				System.out.println(linha);
				linha = "";
			}
			linha += item.getSimbolo()+"  ";			
		}

	}

}
