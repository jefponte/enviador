package br.com.enviador.main;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
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
					Socket conexao = new Socket("177.207.12.26", 12345);
					System.out.println("Vou enviar captura");
					
					
					Robot robot = null;
					
					
					try {
						
						robot = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment()
										.getDefaultScreenDevice());
						Toolkit toolkit = Toolkit.getDefaultToolkit();
						final Dimension dimension = toolkit.getScreenSize();
						int i = 0;
						
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
					
					
//					File f = new File("/home/jefponte/100.JPEG");
//					
//                    @SuppressWarnings("resource")
//                    FileInputStream in1 = new FileInputStream(f);
//                    
//                    OutputStream out = conexao.getOutputStream();
//                    OutputStreamWriter osw = new OutputStreamWriter(out);
//                    BufferedWriter writer = new BufferedWriter(osw);
//                    writer.flush();
//                    int tamanho = 4096;
//                    byte[] buffer = new byte[tamanho];
//                    int lidos = -1;
//                    while ((lidos = in1.read(buffer, 0, tamanho)) != -1) {
//                         out.write(buffer, 0, lidos);
//                         System.out.println("buffo: "+buffer+" tamanho"+tamanho+" lidos:"+lidos);
//                    }
//                    System.out.println("Aweeee");
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
