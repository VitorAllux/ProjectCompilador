package AnalisadorLexico;

public class pilha {
	
	int codigo;
	String simbolo;
	
	pilha(int codigo, String simbolo){
		this.codigo = codigo;
		this.simbolo = simbolo;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getSimbolo() {
		return simbolo;
	}
	
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

}
