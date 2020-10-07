/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_server;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rnavarro
 */
public class HTTPService implements Runnable {
    //modificar para que pueda enviar cualquier archivo y este sea visiblle par ebrir en el navegador
    //cuando se de el nombre en el URL se debe de enviar el archivo o solo modificar el index.html
    //clase que atendera al navegador
    
    private static final Logger LOG = Logger.getLogger(Http_server.class.getName());

    
    
    private Socket clientSocket;
    
    PrintStream out;
    public HTTPService(Socket c) throws IOException {
        clientSocket = c;
        this.out = new PrintStream(clientSocket.getOutputStream(), true);
    }

    @Override
    public void run() {
        BufferedReader in
                = null;
        try {
            //para obtener los datos que enviara el cliente y los datos que enviaremos
            //enviar nuestros datos
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));//recibir los datos del cliente
            String inputLine;
            
            String[] direccion;
            String direc = null;
            // leer la solicitud del cliente
            while ((inputLine = in.readLine()) != null) {                
                
                if (inputLine.startsWith("GET")) {
                    //separar en tokens la lineas, la url, es para obtener el nombre de otro archivo
                    //para obtener el archivo
                    direccion = inputLine.split("\\s+");
                    direc = direccion[1].substring(direccion[1].lastIndexOf("/")+ 1);
                
                }
                //imprime las lineas del encabezado
                System.out.println(inputLine);
                
                //Si recibimos linea en blanco, es fin del la solicitud
                if( inputLine.isEmpty() ) {//se lee el encabezado del http
                    break;
                    //cuando sea en blanco(termine) se saldra del while
                }
            }
            //para identificar el archivo
            File fi = new File(direc);
            if (fi.exists() == false) {
                error404();
            }if (direc.endsWith(".ico") || direc.endsWith(".jpg") || direc.endsWith(".gif") || direc.endsWith(".png")) {
                leerImagen(direc);
            }else{
                leerHtml(fi);
            }
            
            //lee el archivo web, el contenido de nuestra web
            
            System.out.println("El archivo es: ");
            System.out.println(direc);
            
            System.out.println("done");
            clientSocket.close();//cierra conexion con el navegador
        } catch (IOException ex) {
            System.out.println("Error en la conexion");
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void leerHtml(File fi) {
        FileReader file;
        try{file = new FileReader(fi);
            
            //encabezado de respuesta
            out.println("HTTP/1.1 200 OK");//protocolo HTTP  ejecutar
            out.println("Content-Type: text/html; charset=utf-8");//tipo de datos que enviara el servidor al navegador
            //out.println("Content-Length: " + f.length); el f.length regresa la cantidad de bytes del archivo
            out.println("Content-Length: " + fi.length());//contenido de caracteres del archivo a enviar o la cantidad de bytes
            out.println();
            
            int data;
                //se envia byte por byte el archivo nuestro
            while( (data = file.read()) != -1 ) {
                out.write(data);
                     
                    //System.out.println(".");
            }
            out.flush();
            file.close();
        } catch(FileNotFoundException e){
            System.err.println("Error " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void leerImagen(String f){
            //enviar nuestros datos
            //encabezado de respuesta
            File fi = new File(f);
            out.println("HTTP/1.1 200 OK");//protocolo HTTP  ejecutar
            if (f.endsWith(".jpg")) {
                out.println("Content-Type: image/jpeg");//tipo de datos que enviara el servidor al navegador
            } else if (f.endsWith(".ico")) {
                out.println("Content-Type: image/x-icon");
            } else if (f.endsWith(".png")) {
                out.println("Content-Type: image/png");
            } else if (f.endsWith(".gif")) {
                out.println("Content-Type: image/gif");
            }
            //out.println("Content-Length: " + f.length); el f.length regresa la cantidad de bytes del archivo
            out.println("Content-Length: " + fi.length());//contenido de caracteres del archivo a enviar o la cantidad de bytes
            out.println();
            FileInputStream file;
            System.out.println("Tamaño del archivo: " + fi.length());
            try{
                file = new FileInputStream(f);
                int data;
                    //se envia byte por byte el archivo nuestro
                while( (data = file.read()) != -1 ) {
                    out.write(data);
                    //System.out.println(".");
                }
                out.flush();
                file.close();
            } catch(FileNotFoundException e){
                System.err.println("Error:" + e.getMessage());
            } catch (IOException ex) {
            Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void error404(){
        File fi = new File("404.html");
        FileReader file;
        try{file = new FileReader(fi);
            
            //encabezado de respuesta
            out.println("HTTP/1.1 200 OK");//protocolo HTTP  ejecutar
            out.println("Content-Type: text/html; charset=utf-8");//tipo de datos que enviara el servidor al navegador
            //out.println("Content-Length: " + f.length); el f.length regresa la cantidad de bytes del archivo
            out.println("Content-Length: " + fi.length());//contenido de caracteres del archivo a enviar o la cantidad de bytes
            out.println();
            
            int data;
                //se envia byte por byte el archivo nuestro
            while( (data = file.read()) != -1 ) {
                out.write(data);
                     
                    //System.out.println(".");
            }
            out.flush();
            file.close();
        } catch(FileNotFoundException e){
            System.err.println("Error " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(HTTPService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}