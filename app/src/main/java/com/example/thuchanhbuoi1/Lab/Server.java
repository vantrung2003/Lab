package com.example.thuchanhbuoi1.Lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thuchanhbuoi1.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends AppCompatActivity {
    TextView tvMess;
    Button btnSend;
    EditText edtSend;

    private static final int PORT = 8998;
    private List<Socket> clientSockets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        tvMess = findViewById(R.id.tvMess);
        btnSend = findViewById(R.id.btnSend);
        edtSend = findViewById(R.id.edtSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtSend.getText().toString();
                if (!message.isEmpty()) {
                    // Display the input on the server TextView
                    tvMess.append("\nServer: " + message);
                    // Broadcast the message to all connected clients
                    broadcastMessage("Server: " + message);
                    // Clear the input field after sending the message
                    edtSend.setText("");
                }
            }
        });
        startServer();
    }

    private void broadcastMessage(String s) {
        for (Socket socket : clientSockets) {
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startServer() {
        new ServerTask().execute();
    }

    private class ServerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                ServerSocket socket = new ServerSocket(PORT);
                System.out.println("Server started and listening on port " + PORT);
                while (true) {
                    Socket clientSocket = socket.accept();
                    System.out.println("New client connected!");
                    clientSockets.add(clientSocket);
                    new Thread(() -> handleClient(clientSocket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void handleClient(Socket clientSocket) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String mess;
                while ((mess = reader.readLine()) != null) {
                    System.out.println("Received mess: " + mess);
                    broadcastMessage(mess);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    clientSockets.remove(clientSocket);
                    System.out.println("Client disconnected.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
