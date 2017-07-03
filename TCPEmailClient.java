

import java.net.*;
import java.io.*;
import java.util.*;

public class TCPEmailClient {
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

        String mailTo, mailFrom, subject, input;
        String message = "";
        System.out.println(socketIn.readLine());
        while (true) {
            socketOut.println("HELO");
            System.out.println(socketIn.readLine());

            System.out.print("Please enter your email address: ");
            mailFrom = sysIn.readLine();

            System.out.print("Please enter the recipients email: ");
            mailTo = sysIn.readLine();

            System.out.print("Please enter the subject: ");
            subject = sysIn.readLine();

            long createTime = System.nanoTime();
            socketOut.println("MAIL FROM <" + mailFrom + ">");
            System.out.println(socketIn.readLine());
            createTime = System.nanoTime();
            long connectedTime = System.nanoTime() - createTime;
            System.out.println("Message time: " + connectedTime / 1000000 + " ms");

            socketOut.println("RCPT TO <" + mailTo + ">");
            System.out.println(socketIn.readLine());
            connectedTime = System.nanoTime() - createTime;
            System.out.println("Message time: " + connectedTime / 1000000 + " ms");

            createTime = System.nanoTime();
            socketOut.println("DATA ");
            System.out.println(socketIn.readLine());
            connectedTime = System.nanoTime() - createTime;
            System.out.println("Message time: " + connectedTime / 1000000 + " ms");


            message = "To: " + mailTo + "\r\n"
                    + "From: " + mailFrom + "\r\n"
                    + "Subject: " + subject + "\r\n" + "\r\n";
            while (!(input = sysIn.readLine()).equals(".")) {
                message = message + input + "\r\n";
            }

            message = message + ".";

            createTime = System.nanoTime();
            socketOut.println(message);
            System.out.println(socketIn.readLine());
            connectedTime = System.nanoTime() - createTime;
            System.out.println("Message time: " + connectedTime / 1000000 + " ms");

            System.out.print("Enter 'Quit' to end sending emails or 'enter' to send another: ");
            String answer = sysIn.readLine().toUpperCase();
            socketOut.println(answer);
            if (answer.equals("QUIT")) {
                System.out.println(socketIn.readLine());
                break;
            }
        }


            socketOut.close();
            socketIn.close();
            sysIn.close();

            tcpSocket.close();

        }


    }

