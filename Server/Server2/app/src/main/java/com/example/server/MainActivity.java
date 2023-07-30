package com.example.server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView messageTextView;
    private EditText messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageTextView = findViewById(R.id.messageTextView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Gán sự kiện onClick cho button sendButton
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy nội dung từ EditText messageEditText
                String message = messageEditText.getText().toString();

                // Hiển thị nội dung lên TextView messageTextView
                messageTextView.append("\nServer: " + message);

                messageEditText.getText().clear();

                // Gửi nội dung lên máy chủ
                new ClientTask().execute(message);
            }
        });
    }

    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                Socket socket = new Socket("10.0.2.16", 8000); // Thay "server_ip_address" bằng địa chỉ IP của máy chủ

                // Gửi dữ liệu tới máy chủ
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write(params[0]);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                // Đóng kết nối
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
