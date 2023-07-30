package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        final String serverHost = "localhost";
        Socket socketOfClient = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        int port = 8998;

        try {
            socketOfClient = new Socket(serverHost, port);
            br = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
            bw = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

            // Gửi tin nhắn đến Server để bắt đầu trò chuyện
            bw.write("Client connected to server.");
            bw.newLine();
            bw.flush();

            // Giao tiếp với Server
            String message;
            do {
                String responseLine = br.readLine();
                if (responseLine == null) {
                    System.out.println("Server closed the connection.");
                    break;
                }
                System.out.println("Server: " + responseLine);

                if (responseLine.equals("quit")) {
                    System.out.println("Server disconnected.");
                    break;
                }

                System.out.print("Client: ");
                message = System.console().readLine();
                bw.write(message);
                bw.newLine();
                bw.flush();
            } while (!message.equals("quit"));
        } catch (UnknownHostException e) {
            System.out.println("Don't know about this host " + serverHost);
        } catch (IOException e) {
            System.err.println("Could not get I/O ");
        } finally {
            try {
                if (br != null)
                    br.close();
                if (bw != null)
                    bw.close();
                if (socketOfClient != null)
                    socketOfClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
