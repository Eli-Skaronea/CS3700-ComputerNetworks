import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TCPMultiThread extends Thread {
    private Socket clientSocket = null;
    private List<String> requestInfo = new ArrayList<>();

    public TCPMultiThread(Socket socket) {
        super("TCPMultiServerThread");
        clientSocket = socket;
    }

    public void run() {

        try {
            PrintWriter cSocketOut = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader cSocketIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String fromClient;
            String requestLine = null;

            while ((fromClient = cSocketIn.readLine()) != null) {
                while (!(fromClient.equals(""))) {
                    if (requestLine == null) {
                        requestLine = fromClient;
                    }

                    System.out.println(fromClient);
                    fromClient = cSocketIn.readLine();
                    if (fromClient == null) {
                        break;
                    }
                }

                if (fromClient == null) {
                    break;
                }


                getRequestInfo(requestLine);
                System.out.println("");


                String method = requestInfo.get(0);
                String fileName = requestInfo.get(1).substring(1);
                String requestCase = determineMethod(method, fileName);

                String response = createResponseMessage(requestCase);
                cSocketOut.println(response);

                if (requestCase.equals("200 OK")) {
                    String data = readFile(fileName);
                    cSocketOut.println(data);
                }

                requestLine = null;

            }

            cSocketOut.close();
            cSocketIn.close();
            clientSocket.close();

        } catch (IOException error) {
            error.printStackTrace();
        }


    }

    //Method created by "Mr.D" found on Stack Overflow: https://stackoverflow.com/questions/16027229/reading-from-a-text-file-and-storing-in-a-string
    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    private void getRequestInfo(String requestLine){
        requestInfo.clear();
        for (String word : requestLine.split(" ")) {
            requestInfo.add(word);
        }
    }

    private static String createResponseMessage(String requestCase) {
        String statusLine = "HTTP/1.1" + " " + requestCase + "\r\n";
        Date d = new Date();
        String date = "Date: " + d.toString() + "\r\n";
        String server = "Server: Apache/2.0.52 (CentOS) \r\n";
        return statusLine + date + server;
    }


    private static String determineMethod(String method, String fileName) {
        File f = new File(fileName);

        if (method.equals("GET") && (f.isFile())) {
            return "200 OK";
        } else if ((method.equals("GET")) && !f.isFile()) {
            return "404 Not Found";
        } else {
            return "400 Bad Request";
        }

    }
}

