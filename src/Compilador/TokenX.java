package Compilador;

public class TokenX {
	int codigo;
	String simbolo;
	
	public TokenX() {
		
	}
	
	public TokenX(int codigo, String simbolo){
		this.codigo = codigo;
		this.simbolo = simbolo;
	}
		
	public Integer getCodigo() {
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
