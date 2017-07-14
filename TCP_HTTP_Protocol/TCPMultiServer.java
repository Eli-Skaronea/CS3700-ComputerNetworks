import java.net.*;
import java.io.*;


public class TCPMultiServer {
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = null;
        boolean listen = true;

        try {
            serverSocket = new ServerSocket(5160);
        } catch(IOException error){
            System.err.println("Could not listen port: 5160");
            System.exit(-1);
        }

        while(listen){
            new TCPMultiThreadEmail(serverSocket.accept()).start();
        }

        serverSocket.close();

    }
}
