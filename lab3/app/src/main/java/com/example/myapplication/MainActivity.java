package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    TextView resultText;
    private static final String BASE_URL_NASA = "https://api.nasa.gov/planetary/apod?api_key=IP5pVaHaWYJXW1YFdrA03mXo4IayAmPLytGphJqi&date=";

    ListView listView;
    Executor executor;
    Button bntpost;

    List<NasaItem> nasaItemList = new ArrayList<>(); // Mảng lưu trữ các đối tượng NasaItem
    int fetchedItemCount = 0; // Số lượng dữ liệu đã được lấy từ API

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lisview);
        resultText = findViewById(R.id.tvteview);
        bntpost = findViewById(R.id.bntpost);
        bntpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // post tại đây
                for (NasaItem item : nasaItemList) {
                    postNasaDataToServer(item);
                }
            }
        });

        executor = Executors.newFixedThreadPool(5); // Sử dụng Executor với pool có 5 luồng (1 luồng/ngày)

    }

    private void postNasaDataToServer(NasaItem item) {
        OkHttpClient client = new OkHttpClient();

        // Set up the request body using the data from NasaItem
        RequestBody requestBody = new FormBody.Builder()
                .add("title", item.getTitle())
                .add("date", item.getDate())
                .add("base64Image", item.getBase64Image())
                .build();

        // Create the POST request to your server
        Request request = new Request.Builder()
                .url("http://localhost:3000/up") // Replace "YOUR_SERVER_URL" with your server URL
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the failure here
                e.printStackTrace();
                // Show failure toast
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Failed to post data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response here
                if (response.isSuccessful()) {
                    // Data successfully sent to the server
                    String responseData = response.body().string();
                    Log.d("TAG", responseData);
                    // Show success toast
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Data successfully posted.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Server returned an error
                    Log.e("TAG", "Error sending data to server. Code: " + response.code());
                    // Show failure toast
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Failed to post data.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // Don't forget to close the response to release resources properly.
                response.close();
            }
        });
    }

    public void onClickSendGetListHttpUrlConnectionnsa(View view) {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // Reset dữ liệu
        nasaItemList.clear();
        fetchedItemCount = 0;

        // Gọi phương thức thực thi các tác vụ liên quan đến mạng và tạo Base64 cho 5 ngày khác nhau
        for (int i = 0; i < 8; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Lấy ngày trước đó
            String date = sdf.format(calendar.getTime()); // Chuyển định dạng ngày

            // Gửi yêu cầu để lấy thông tin từ API của NASA cho ngày hiện tại
            String url = BASE_URL_NASA + date;
            executor.execute(new GetNasaDataRunnable(url));
        }
    }

    // Runnable để lấy dữ liệu của NASA cho một ngày cụ thể
    private class GetNasaDataRunnable implements Runnable {
        private final String apiUrl;

        GetNasaDataRunnable(String apiUrl) {
            this.apiUrl = apiUrl;
        }

        @Override
        public void run() {
            try {
                // Gửi yêu cầu để lấy thông tin từ API của NASA
                URL nasaUrl = new URL(apiUrl);
                HttpURLConnection nasaConn = (HttpURLConnection) nasaUrl.openConnection();
                nasaConn.setRequestMethod("GET");
                nasaConn.setRequestProperty("Content-Type", "application/json");
                nasaConn.setRequestProperty("Accept", "application/json");

                int nasaResponseCode = nasaConn.getResponseCode();

                if (nasaResponseCode == HttpURLConnection.HTTP_OK) {
                    InputStream nasaResponseBody = nasaConn.getInputStream();
                    InputStreamReader nasaResponseBodyReader = new InputStreamReader(nasaResponseBody, StandardCharsets.UTF_8);

                    JsonReader nasaJsonReader = new JsonReader(nasaResponseBodyReader);
                    nasaJsonReader.beginObject();

                    String title = "", date = "", copyright = "", imgUrl = "";

                    while (nasaJsonReader.hasNext()) {
                        String key = nasaJsonReader.nextName();
                        if (key.equals("title")) {
                            title = nasaJsonReader.nextString();
                        } else if (key.equals("date")) {
                            date = nasaJsonReader.nextString();
                        } else if (key.equals("copyright")) {
                            copyright = nasaJsonReader.nextString();
                        } else if (key.equals("url")) {
                            imgUrl = nasaJsonReader.nextString();
                        } else {
                            nasaJsonReader.skipValue();
                        }
                    }

                    nasaJsonReader.endObject();
                    nasaJsonReader.close();

                    // Thực hiện lấy hình ảnh và tạo Base64 bằng cách sử dụng AsyncTask
                    new ImageToBase64Task().execute(imgUrl, title, date, copyright);
                } else {
                    // Xử lý phản hồi lỗi từ API của NASA
                    String error = "NASA API Error: " + nasaResponseCode;
                    resultText.setText(error);
                }

                nasaConn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // AsyncTask để lấy hình ảnh và tạo Base64
    private class ImageToBase64Task extends AsyncTask<String, Void, NasaItem> {

        @Override
        protected NasaItem doInBackground(String... params) {
            String imgUrl = params[0];
            String title = params[1];
            String date = params[2];
            String copyright = params[3];

            try {
                // Lấy hình ảnh từ URL
                Bitmap bitmap = getBitmapFromUrl(imgUrl);

                // Tạo mã Base64 từ hình ảnh
                String base64Image = convertToBase64(bitmap);
                Log.d("TAG", "ma" + base64Image);
                // Trả về đối tượng NasaItem đã cập nhật thông tin hình ảnh Base64
                return new NasaItem(title, date, copyright, imgUrl, base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NasaItem nasaItem) {
            super.onPostExecute(nasaItem);
            if (nasaItem != null) {
                // Thêm đối tượng NasaItem vào mảng (sử dụng cơ chế đồng bộ hóa)
                synchronized (nasaItemList) {
                    nasaItemList.add(nasaItem);
                    fetchedItemCount++;

                    // Kiểm tra xem đã lấy đủ dữ liệu cho 5 ngày hay chưa
                    if (fetchedItemCount == 8) {
                        // Hiển thị danh sách các mục lên ListView
                        NasaItemAdapter adapter = new NasaItemAdapter(MainActivity.this, nasaItemList);
                        listView.setAdapter(adapter);
                    }
                }
            }
        }
    }

    // Hàm lấy hình ảnh từ URL
    private Bitmap getBitmapFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

    // Hàm chuyển đổi hình ảnh sang Base64
    private String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
    }
}
