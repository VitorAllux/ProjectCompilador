package AnalisadorLexico;

import java.util.Stack;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		automatos auto = new automatos();
		
		Stack<pilha> lista = auto.splitSimbols("if 20 > 10 then begin", 1);
		
		String str = "";
		
		for(pilha pilha : lista) {
			str = str + ' ' + pilha.getSimbolo();		
		}
		
		System.out.println(str);

	}

}
