package br.com.enviador.main;

import br.com.enviador.controller.ServidorController;

public class MainServidor {

	public static void main(String[] args) {
		
		ServidorController controller = new ServidorController();
		controller.iniciaServico();
		controller.iniciaServicoCaptura();
		controller.iniciaAdministrador();
		
		
	}
	
}
