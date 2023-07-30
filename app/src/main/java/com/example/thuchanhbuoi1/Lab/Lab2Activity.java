package com.example.thuchanhbuoi1.Lab;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.example.thuchanhbuoi1.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Lab2Activity extends AppCompatActivity {
    private TextView tvName1;
    private StringBuilder resultBuilder = new StringBuilder();
    private int numThreads = 26; // Số lượng thread sẽ sử dụng
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2);
        tvName1 = findViewById(R.id.tvName1);

        for (char c = 'a'; c <= 'z'; c++) {
            new HashingThread(c).start();
        }
        
    }
    private class HashingThread extends Thread {
        private char character;

        public HashingThread(char character) {
            this.character = character;
        }

        @Override
        public void run() {
            String input = String.valueOf(character);
            try {
                String hashedResult = performHashing(input);
                resultBuilder.append(character).append(": ").append(hashedResult).append("\n");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            numThreads--;
            if (numThreads == 0) {
                // Hết tất cả các thread, gửi kết quả lên Handler để cập nhật UI
                resultHandler.sendEmptyMessage(0);
            }
        }
    }

    // Handler để cập nhật kết quả lên TextView
    private Handler resultHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tvName1.setText("Hashed results:\n" + resultBuilder.toString());
            return true;
        }
    });

    // Function để thực hiện hashing
    private String performHashing(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());

        byte[] byteData = md.digest();

        // Chuyển mảng byte thành chuỗi hexa
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : byteData) {
            stringBuilder.append(String.format("%02x", b));
        }

        return stringBuilder.toString();
    }
}