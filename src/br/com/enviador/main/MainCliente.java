package br.com.enviador.main;

import br.com.enviador.controller.ClienteController;

public class MainCliente {
	
	public static void main(String[] args) {
		System.out.println("Iniciar Cliente.");
		ClienteController controller = new ClienteController();
		controller.tentandoConexao();
		
	}

}
