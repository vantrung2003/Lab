package com.example.thuchanhbuoi1.ThucHanh;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thuchanhbuoi1.R;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ThucHanh extends AppCompatActivity {
        private TextView tvName;
        private Button btnGet, btnPost;
        private  static  final  String GET_URL ="http://103.118.28.46:3000/get-quote";
        private  static  final  String POST_URLL="http://103.118.28.46:3000/add-quote";
            @SuppressLint("MissingInflatedId")
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_thuc_hanh);
                  tvName = findViewById(R.id.tvName1);
                  btnGet= findViewById(R.id.btnGet);
                  btnPost= findViewById(R.id.btnPost);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                btnGet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickSendGet(tvName);
                    }
                });
                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       sendPost(tvName);
                    }
                });


            }
            // Sử dụng phương thức get, để lấy dữ liệu từ server
            private  void  sendGet() throws Exception{
                URL url = new URL(GET_URL);
                // Mở giao thức http
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                // Phuong thuc GET
                   connect.setRequestMethod("GET");
                 // congif responsive
                  connect.setRequestProperty("Content", "application/json");
                  connect.setRequestProperty("Accecpt","application/json");

              //nhan respon code
                int responcode =  connect.getResponseCode();

                if(responcode == HttpURLConnection.HTTP_OK)
                //Xu ly viec du lieu duoc tra ve
                {
                    InputStream responnn = connect.getInputStream();
                    InputStreamReader responbody  = new InputStreamReader(responnn,"UTF-8");

                    //Send request
                    JsonReader jsonReader = new JsonReader(responbody);
                    jsonReader.beginObject();
                    //Su ly json object de xu ly du lieu
                    String quote = xuLyDuLieu("quote",jsonReader);
                    tvName.setText(quote);

                }else {
                    //Su ly response tra ve error
                    System.out.println("Error");
                }
            }

            //funtion xu ly du liệu

            private   String  xuLyDuLieu(String key, JsonReader reader) throws   Exception{
               String value ="";

                while (reader.hasNext()) // đọc json cho đến hết thì
                {
                    String k = reader.nextName();
                    if(k.equals(key)){
                        value = reader.nextString();
                        break;
                    }else {
                        reader.skipValue();
                        System.out.println(".....");
                    }
                }

                return value;
            }

                    public  void onClickSendGet(View v){
                    try{
                            sendGet();
                    }catch (Exception e){
                            e.printStackTrace();
                        }
            }

            public  void sendPost(View v){
                try {
                    POST_URL();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //post
            private  void POST_URL() throws Exception{
                URL url = new URL(POST_URLL);
                // Mở giao thức http
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                // Phuong thuc GET
                connect.setRequestMethod("GET");
                // congif responsive
                connect.setRequestProperty("Content", "application/json");
                connect.setRequestProperty("Accecpt","application/json");
                connect.setDoInput(true);
                //set data body
                JSONObject object = new JSONObject();
                object.put("name","Trần Văn Trung - Thái Bình!!!");

                byte[] postData = object.toString().getBytes(StandardCharsets.UTF_8);
                connect.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connect.setDoOutput(true);
                try (OutputStream outputStream = connect.getOutputStream()) {
                    outputStream.write(postData);
                }
                //nhan respon code
                int responcode =  connect.getResponseCode();
                if(responcode == HttpURLConnection.HTTP_OK)
                //Xu ly viec du lieu duoc tra ve
                {
                    InputStream responnn = connect.getInputStream();
                    InputStreamReader responbody  = new InputStreamReader(responnn,"UTF-8");

                    //Send request
                    JsonReader jsonReader = new JsonReader(responbody);
                    jsonReader.beginObject();
                    //Su ly json object de xu ly du lieu
                    String quote = xuLyDuLieu("message",jsonReader);
                    tvName.setText(quote);

                }else {
                    //Su ly response tra ve error
                    System.out.println("Error");
                }

            }


    }