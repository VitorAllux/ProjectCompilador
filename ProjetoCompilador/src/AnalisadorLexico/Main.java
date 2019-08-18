package AnalisadorLexico;

import java.util.Stack;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Automatos auto = new Automatos();
		
		Stack<Pilha> lista = auto.splitSimbols("if 20 > 10 then begin", 1);
		
		String str = "";
		
		for(Pilha pilha : lista) {
			str = str + ' ' + pilha.getSimbolo();		
		}
		
		System.out.println(str);

	}

}
