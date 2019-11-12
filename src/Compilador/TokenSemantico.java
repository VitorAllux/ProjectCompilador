package Compilador;

public class TokenSemantico extends TokenX{
	
	private String tipo = "", categoria = "";
	private Integer nivel = 0;

	public TokenSemantico() {
	}
	
	public TokenSemantico(Token simbolo, Integer nivel, String Categoria) {
		
		
		
	}	
	
	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	

}
