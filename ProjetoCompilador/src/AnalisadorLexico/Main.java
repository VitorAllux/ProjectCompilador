package AnalisadorLexico;

import java.util.Stack;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Automatos auto = new Automatos();

		Stack<Pilha> lista = auto.splitSimbols("Procedure P; (*isto é um comentario #*) ", 1);
		lista = auto.splitSimbols("Var", 1);
		lista = auto.splitSimbols(" A :(*teste coment*)integer;", 1);
		lista = auto.splitSimbols("Begin", 1);
		lista = auto.splitSimbols(" Readln(a);", 1);
		lista = auto.splitSimbols(" If a=x then", 1);
		lista = auto.splitSimbols(" z:=z+x", 1);
		lista = auto.splitSimbols(" Else begin", 1);
		lista = auto.splitSimbols(" Z:=z-x;", 1);
		lista = auto.splitSimbols(" Call p;", 1);
		lista = auto.splitSimbols(" End;", 1);
		lista = auto.splitSimbols("End; ", 1);

		String str = "";

		for(Pilha pilha : lista) {
			str = str + ' ' + pilha.getSimbolo();		
		}

		System.out.println(str);

	}

}
