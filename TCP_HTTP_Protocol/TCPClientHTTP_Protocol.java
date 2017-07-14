

import java.net.*;
import java.io.*;
import java.util.*;

public class TCPClient {
    private static List<String> requestData = new ArrayList<>();
    private static BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) throws IOException {
        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        System.out.print("Please enter the ip/DNS Name of HTTP SERVER: ");
        String dns_ip = sysIn.readLine();


        try {
            long createTime = System.nanoTime();
            tcpSocket = new Socket(dns_ip, 5160);
            long connectedTime = System.nanoTime() - createTime;
            System.out.println("Connection time: " + connectedTime / 1000000 + " ms");

            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + dns_ip);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + dns_ip);
            System.exit(1);
        }


        while (true) {
            getRequestData();

            String requestMessage = createRequestMessage(dns_ip);
            long createTime = System.nanoTime();
            socketOut.println(requestMessage);

            String response;
            String responseCode = null;
            while (!(response = socketIn.readLine()).equals("")) {
                if(responseCode == null){
                    responseCode = response.substring(9,12);
                }
                System.out.println(response);
            }
            System.out.println(response);
            long finishTime = System.nanoTime() - createTime;
            System.out.println("Message Response Time: " + finishTime / 1000000 + " ms");
            System.out.println("");

            if(responseCode.equals("200")){
                String fileName = requestData.get(1);
                File file = new File(fileName);
                if (file.isFile()) {
                    file.delete();

                }
                String data;
                int counter = 0;

                while ((counter < 4)) {
                    if ((data = socketIn.readLine()).equals("")) {
                        counter++;

                    } else {
                        counter = 0;
                    }
                    FileWriter writer = new FileWriter(fileName, true);
                    writer.append(data);
                    writer.write("\r\n");
                    writer.close();

                    if(data == null){
                        break;
                    }

                }
            }




            System.out.print("Would you like to do another? <y/n>: ");
            String again = sysIn.readLine();
            if (again.equals("n") || again.equals("N") || again.equals("No") || again.equals("NO")) {
                break;
            }



        }

        socketOut.close();
        socketIn.close();
        sysIn.close();

        tcpSocket.close();

    }


    private static String createRequestMessage(String dns_ip) {
        String requestLine = requestData.get(0) + " /" + requestData.get(1)
                + " " + requestData.get(2) + "\r\n";
        String hostHeaderLine = "Host: " + dns_ip + "\r\n";
        String userAgentHeaderLine = "User-Agent: " + requestData.get(3) + "\r\n";
        return requestLine + hostHeaderLine + userAgentHeaderLine;
    }

    private static void getRequestData() throws IOException {
        requestData.clear();
        System.out.print("Enter the method (GET, POST, HEAD...): ");
        requestData.add(sysIn.readLine().toUpperCase());
        System.out.print("Enter the file name you wish to get <case sensitive> (example.htm): ");
        requestData.add(sysIn.readLine());
        System.out.print("Enter the HTTP Version (1.1,1.2): ");
        requestData.add("HTTP/" + sysIn.readLine());
        System.out.print("Enter the User-Agent: ");
        requestData.add(sysIn.readLine());
        System.out.println("");
    }

}
