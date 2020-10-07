/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 *
 * @author rnavarro
 */
public class Http_server {
    
    public static final int HTTP_PORT = 8080;
    
    private static final Logger LOG = Logger.getLogger(Http_server.class.getName());
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //asegurarse que sea http en lugar de https en la direccion web, el url de abajo debe de ir en el navegador
        //http://localhost:8080
        try {//empieza a crear el servidor
            ServerSocket serverSocket
                    = new ServerSocket( HTTP_PORT  );//pasamos el numero del puerto a utilizar
            
            // 
            while (true) {//Para la solucisitud
                Socket clientSocket = serverSocket.accept();//cuando accedan se creara un socket
                LOG.info("CONNECT");
                
                //se le dara un hilo al usuario que haya entrado
                Thread serviceProcess = new Thread(new HTTPService(clientSocket));
                
                serviceProcess.start();
            }

        } catch (IOException e) {
            LOG.warning("Exception caught when trying to listen on port "
                    + HTTP_PORT + " or listening for a connection");
            LOG.warning(e.getMessage());
        }
    }
    
}
