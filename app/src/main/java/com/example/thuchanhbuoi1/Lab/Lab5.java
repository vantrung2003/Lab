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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Lab5 extends AppCompatActivity {
    private TextView messageTextView;
    private EditText messageEditText;
    private Button sendButton;

    final String serverHost = "10.0.2.16";
    Socket socketOfClient = null;
    BufferedReader br = null;
    BufferedWriter bw = null;
    int port = 6969;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab5);
        messageTextView = findViewById(R.id.messageTextView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Thiết lập sự kiện onClick cho Button sendButton
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy nội dung từ EditText messageEditText
                String message = messageEditText.getText().toString();

                // Gửi nội dung lên máy chủ
                new ClientTask().execute(message);

                // Hiển thị nội dung đã gửi lên TextView messageTextView
                messageTextView.append("\nClient: " + message);

                // Xóa nội dung đã nhập trong EditText messageEditText
                messageEditText.setText("");
            }
        });

        // Kết nối tới máy chủ và lắng nghe dữ liệu từ máy chủ trong một AsyncTask
        new ConnectToServerTask().execute();
    }

    private class ConnectToServerTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                socketOfClient = new Socket(serverHost, port);
                br = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
                bw = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

                // Gửi tin nhắn thông báo kết nối thành công
                bw.write("Client connected to server");
                bw.newLine();
                bw.flush();

                // Lắng nghe dữ liệu từ máy chủ và hiển thị lên TextView messageTextView
                String response;
                while ((response = br.readLine()) != null) {
                    publishProgress(response);
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // Hiển thị dữ liệu từ máy chủ lên TextView messageTextView
            messageTextView.append("\nServer: " + values[0]);
        }
    }

    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                if (bw != null) {
                    // Gửi dữ liệu tới máy chủ
                    bw.write(params[0]);
                    bw.newLine();
                    bw.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
