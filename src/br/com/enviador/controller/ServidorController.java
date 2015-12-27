package br.com.enviador.controller;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import br.com.enviador.model.Cliente;


public class ServidorController {

	public ArrayList<Cliente>listaDeClientes;
	public ServerSocket serverSocket;
	
	public ServidorController(){
		
	}
	
	
	public void iniciaServico(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				System.out.println("Servidor Iniciando...");
				int porta = 37389;
				System.out.println("Abrindo conexão na porta "+porta);
				try {
					
					serverSocket = new ServerSocket(porta, 10);
					System.out.println("Seridor Iniciado, aguardando conexões. ");
					Socket socketCliente;
					while(true){
						socketCliente = serverSocket.accept();
						System.out.println("Nova conexão. Vamos processa-la.");
						processandoConexao(socketCliente);
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		//Inicia servidor 
		//Espera conexões. 

		
	}
	public void processandoConexao(final Socket socketCliente){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Cliente cliente = new Cliente();
				cliente.setConxao(socketCliente);
				listaDeClientes.add(cliente);
				
				try {
					ObjectInputStream entrada = new ObjectInputStream(socketCliente.getInputStream());
					while(true){
						try {
							String mensagem = (String) entrada.readObject();
							
						} catch (ClassNotFoundException e) {
							
							System.out.println("Erro maluco, cliente desconectado. ");
							cliente.getConxao().close();
							listaDeClientes.remove(cliente);
							break;
						}
						
					}
				} catch (IOException e) {
					System.out.println("Erro maluco, cliente desconectado. ");
					try {
						cliente.getConxao().close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					listaDeClientes.remove(cliente);
					
				}
				listaDeClientes.remove(cliente);
				
			}
		});
		
	}
	
	
	public void processandoMensagens(final String mensagem, final Cliente cliente){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				if(mensagem.contains("setNome") && mensagem.length() >= "setNome".length()+2){
					cliente.setNome(mensagem.substring("setNome(".length(), mensagem.length()-1 ));
					System.out.println("Novo nome é: "+cliente.getNome());
				}
				
				System.out.println(cliente.getNome()+">>"+mensagem);
				
				
			}
		});
			
	}
	public void iniciaAdministrador(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				System.out.println("Administrador Iniciando...");
				
				Scanner leitor = new Scanner(System.in);
				String comando = "";
				do{
					System.out.println("Digite um comando ao servidor: ");
					comando = leitor.nextLine();
					System.out.println("Você digitou: "+comando);
					
				}while(!comando.equals("encerrar"));
				System.out.println("Encerrando servidor.");
				System.exit(0);
			}
		});
		//Digite um comando para o servidor
		
	}
}
