package AnalisadorLexico;

public class Pilha {
	
	int codigo;
	int linha;
	String simbolo;
	
	Pilha(int codigo, int linha, String simbolo){
		this.codigo = codigo;
		this.simbolo = simbolo;
		this.linha = linha;
	}
	
	public void setLinha(int linha) {
		this.linha = linha;
	}
	
	public int getLinha() {
		return linha;
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
