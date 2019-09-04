package AnalisadorLexico;

public class Erro {
	
	private String MsgError;
	private int linha;
	private String tipo;

	public Erro(String MsgError, String tipo, int linha) {
		this.MsgError = MsgError;
		this.linha = linha;
		this.tipo = tipo;
	}

	public String getMsgError() {
		return MsgError;
	}
	
	public void setMsgError(String msgError) {
		MsgError = msgError;
	}
	
	public int getLinha() {
		return linha;
	}
	
	public void setLinha(int linha) {
		this.linha = linha;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
