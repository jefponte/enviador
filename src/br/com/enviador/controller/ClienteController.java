package br.com.enviador.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import br.com.enviador.model.Cliente;

public class ClienteController {


	private Cliente cliente;
	
	public void tentandoConexao(){
		
		this.cliente = new Cliente();
		Socket conexao;
		try {
			conexao = new Socket("localhost", 37389);
			this.cliente.setConxao(conexao);
			
		} catch (UnknownHostException e) {
			System.out.println("Servidor Indisponivel");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Erro de IO");
			e.printStackTrace();
		}
		
	}
	
	
}
