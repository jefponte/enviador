package br.com.enviador.main;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

/**
 * Essa classe vai ser chamada pelo cliente quando quiser 
 * enviar uma captura. 
 * @author jefponte
 *
 */
public class MainCaptura {

	public void tentarConectar(){
		Thread enviar = new Thread(new Runnable() {
			
			@Override
			public void run() {


				try {
					Socket conexao = new Socket("localhost", 12345);
					System.out.println("Vou enviar captura");
					
					
					Robot robot = null;
					
					
					try {
						
						robot = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment()
										.getDefaultScreenDevice());
						Toolkit toolkit = Toolkit.getDefaultToolkit();
						final Dimension dimension = toolkit.getScreenSize();
						
						BufferedImage bi = robot.createScreenCapture(new Rectangle(0, 0,
									dimension.width, dimension.height));
						ImageIO.write(bi, "JPEG", conexao.getOutputStream());
							// Faz a thread dormir 2 minutos.
						
						
						
					} catch (HeadlessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (AWTException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

                    conexao.close();
                    
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		enviar.start();
		
		
	}
	
	
}
