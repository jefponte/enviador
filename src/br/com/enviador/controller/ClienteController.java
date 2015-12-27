package br.com.enviador.controller;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import br.com.enviador.model.Cliente;

public class ClienteController {


	private Cliente cliente;
	
	public void tentandoConexao(){
		
		this.cliente = new Cliente();
		

		Thread tentandoConexao = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					int tentativa = 0;
					System.out.println("Tentativa "+tentativa);
					
					try {
						Socket conexao = new Socket("localhost", 37389);
						processandoConexao(conexao);
						break;
					} catch (UnknownHostException e) {
						System.out.println("Servidor Indisponivel");
						
					} catch (IOException e) {
						System.out.println("Erro de IO");
						
					}			
					
					
					tentativa++;
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		
		tentandoConexao.start();
		
		
		
		
	}
	public void processandoConexao(final Socket conexao){
		cliente.setConxao(conexao);
		Thread processando = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ObjectOutputStream saida;
				ObjectInputStream entrada;
				try {
					saida = new ObjectOutputStream(conexao.getOutputStream());
					saida.flush();
					saida.writeObject("setNome(clienteTeste)");
					saida.flush();
					
					entrada = new ObjectInputStream(conexao.getInputStream());
					
					cliente.setSaida(saida);
					cliente.setEntrada(entrada);
					
					while(true){
						String mensagem = (String) entrada.readObject();
						processandoMensagem(mensagem);
					}
					
					
				} catch (IOException | ClassNotFoundException e) {
					System.out.println("Eror de Io ");
				}
				
				
				
			}
		});
		processando.start();
	}
	public void processandoMensagem(final String mensagem){
		Thread processando = new Thread(new Runnable() {
			public void run() {
			
				System.out.println("Servidor disse: "+mensagem);
				if(mensagem.contains("abreJanelinha")){
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								JFrame frame = new JFrame();
								frame.setSize(300, 300);
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				
			}
		});
		processando.start();
	}
	
}
