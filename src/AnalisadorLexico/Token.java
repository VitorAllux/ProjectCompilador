package AnalisadorLexico;

public class Token extends TokenX{
	
	int linha;	
	
	public Token(int codigo, int linha, String simbolo){
		this.codigo = codigo;
		this.linha = linha;
		this.simbolo = simbolo;
	}
	
	public void setLinha(int linha) {
		this.linha = linha;
	}
	
	public int getLinha() {
		return linha;
	}

}
