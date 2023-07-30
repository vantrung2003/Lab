package socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
     public static void main(String[] args) throws Exception {
          ServerSocket listener = null;
          int clientServer = 0;
          int port = 8998;
          try {
               listener = new ServerSocket(port);

          } catch (Exception e) {
               System.out.println("ERROR!!!");
               System.exit(1);
          }

          try {
               while (true) {
                    Socket serSocket = listener.accept();// accept ket noi socket

                    // tao 1 luong rieng cho client nay chay
                    new ServerThread(serSocket, clientServer).start();
               }
          } finally {
               listener.close();
          }

          // SU dung ServerThread

     }
}

// Tao Thread
class ServerThread extends Thread {
     private int clientServer;
     private Socket socketServer;
     private BufferedReader br;
     private BufferedWriter bw;

     public ServerThread(Socket _socketServer, int _clientServer) {
          this.clientServer = _clientServer;
          this.socketServer = _socketServer;
          try {
               br = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
               bw = new BufferedWriter(new OutputStreamWriter(socketServer.getOutputStream()));
          } catch (IOException e) {
               e.printStackTrace();
          }
     }

     public void run() {
          try {
               System.out.println("Client " + clientServer + " connected.");
               String receivedMessage;
               do {
                    receivedMessage = br.readLine();
                    if (receivedMessage == null || receivedMessage.equals("quit")) {
                         System.out.println("Client " + clientServer + " disconnected");
                         break;
                    }
                    System.out.println("Client " + clientServer + ": " + receivedMessage);

                    System.out.print("Server: ");
                    String response = System.console().readLine();
                    bw.write(response);
                    bw.newLine();
                    bw.flush();
               } while (true);
          } catch (IOException e) {
               System.out.println("Client " + clientServer + " disconnected");
          } finally {
               try {
                    socketServer.close();
               } catch (IOException e) {
                    e.printStackTrace();
               }
          }
     }
}
