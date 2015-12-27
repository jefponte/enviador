package br.com.enviador.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import br.com.enviador.model.Cliente;

public class ClienteController {


	private Cliente cliente;
	
	public void tentandoConexao(){
	
		
		/*
		 * Ao conectar iremos enviar alguns dados necessários 
		 * para identificação do cliente. Nome da máquina. 
		 * Depois vamos processar mensagens vindas do servidor pra 
		 * executar funções de controle da máquina do cliente. 
		 * 
		 */
		
		this.cliente = new Cliente();
		Socket conexao;
		try {
			conexao = new Socket("localhost", 37389);
			this.cliente.setConxao(conexao);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
