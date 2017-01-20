package br.com.enviador.model;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Cliente {

	private String nome;
	private Socket conexao;
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;
	public Socket getConexao() {
		return conexao;
	}
	public void setConexao(Socket conexao) {
		this.conexao = conexao;
	}
	public ObjectOutputStream getSaida() {
		return saida;
	}
	public void setSaida(ObjectOutputStream saida) {
		this.saida = saida;
	}
	public ObjectInputStream getEntrada() {
		return entrada;
	}
	public void setEntrada(ObjectInputStream entrada) {
		this.entrada = entrada;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
