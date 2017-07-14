import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TCPMultiThreadEmail extends Thread {
    private Socket clientSocket = null;
    private List<String> requestInfo = new ArrayList<>();

    public TCPMultiThreadEmail(Socket socket) {
        super("TCPMultiServerThread");
        clientSocket = socket;
    }

    public void run() {

        try {
            PrintWriter cSocketOut = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader cSocketIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String fromClient;

            String clientIP = clientSocket.getLocalAddress().toString().substring(1);
            System.out.println("Connected to <" + clientIP + ">");
            cSocketOut.println("220 " + clientIP);

            while ((fromClient = cSocketIn.readLine()) != null) {
                String helloLine = null;
                String mailFrom = null;
                String rcptTo = null;
                String data = null;
                String message = "";


                while (helloLine == null) {
                    if (fromClient.toUpperCase().startsWith("HELO")) {
                        helloLine = fromClient;
                        System.out.println(fromClient);
                        cSocketOut.println("250 " + clientIP + " Helo " + clientIP);
                    } else {
                        cSocketOut.println("503 5.5.2 Send 'HELO' first");
                        fromClient = cSocketIn.readLine();
                    }
                }

                while (mailFrom == null) {
                    fromClient = cSocketIn.readLine();
                    if (fromClient.toUpperCase().startsWith("MAIL FROM")) {
                        mailFrom = fromClient + "\r\n";
                        System.out.println(fromClient);
                        cSocketOut.println("250 2.1.0 Sender OK");
                    } else {
                        cSocketOut.println("503 5.5.2 Need 'MAIL FROM' command");

                    }
                }

                while (rcptTo == null) {
                    fromClient = cSocketIn.readLine();

                    if (fromClient.toUpperCase().startsWith("RCPT TO")) {
                        rcptTo = fromClient + "\r\n";
                        System.out.println(fromClient);
                        cSocketOut.println("250 2.1.5 Recipient OK");
                    } else {
                        cSocketOut.println("503 5.5.2 Need 'RCPT TO' command");

                    }
                }

                while (data == null) {
                    fromClient = cSocketIn.readLine();

                    if (fromClient.toUpperCase().startsWith("DATA")) {
                        data = fromClient;
                        System.out.println(fromClient);
                        System.out.println("");
                        cSocketOut.println("354 Start mail input; end with <CRLF>.<CRLF>");


                    } else {
                        cSocketOut.println("503 5.5.2 Need 'DATA' command");
                    }
                }

                while (!(fromClient = cSocketIn.readLine()).equals(".")){
                    message = message + fromClient + "\r\n";
                }

                cSocketOut.println("250 Message received and to be delivered");
                System.out.println(message);
                String quit = cSocketIn.readLine().toUpperCase();
                if (quit.equals("QUIT")) {
                    cSocketOut.println("221 " + clientIP + " closing connection");
                    break;
                }


            }

            cSocketOut.close();
            cSocketIn.close();
            clientSocket.close();

        } catch (IOException error) {
            error.printStackTrace();
        }


    }

}

