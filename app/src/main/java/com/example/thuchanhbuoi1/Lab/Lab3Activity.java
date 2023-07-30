package com.example.thuchanhbuoi1.Lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.thuchanhbuoi1.R;

import org.json.JSONArray;


public class Lab3Activity extends AppCompatActivity {
private TextView tvName ;
private Button btnGet;
private  static  final  String GET_URL ="https://api.nasa.gov/planetary/apod?api_key=x7EF6cd0q7DXhkNKwfYQteyrsRtaVcxtfClfrWLs&date=2023-07-01";
final long startTime = System.currentTimeMillis();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);
        tvName = findViewById(R.id.tvName1);
        btnGet= findViewById(R.id.btnGet);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendJsonArrayRequest();
            }
        });
    }

    private void sendJsonArrayRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, GET_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the JSON Array response
                        try {
                            long endTime = System.currentTimeMillis(); // Ghi lại thời gian sau khi nhận phản hồi
                            long delay = endTime - startTime;  // Thời gian kết thúc - thời gian bắt đầu
                            String result = "Dữ liệu: " + response.toString() + "\nĐộ trễ: " + delay + "ms";
                            tvName.setText(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors, including TimeoutError
                        if (error instanceof TimeoutError) {
                            tvName.setText("Request timed out");
                        } else {
                            tvName.setText("Error: " + error.toString());
                        }
                    }
                });

        // Set the request timeout  delay
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000, // 20 seconds timeout
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));


        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }



}