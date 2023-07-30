package com.example.thuchanhbuoi1.ThucHanh;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thuchanhbuoi1.R;
import com.example.thuchanhbuoi1.api.ApiService;
import com.example.thuchanhbuoi1.api.ReTroFitClient;
import com.example.thuchanhbuoi1.model.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRetrofit extends AppCompatActivity {
private TextView tvname,tvpublisher;
ImageView tvimageurl;
ListView lv;
private Button btnGet;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_retrofit);
        tvname = findViewById(R.id.tvname);
        tvpublisher = findViewById(R.id.tvpublisher);
        tvimageurl = findViewById(R.id.tvimageurl);
        btnGet = findViewById(R.id.btnGet);
//        lv = findViewById(R.id.lv);

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 clickCallApi();
            }
        });

    }

    private void clickCallApi() {
        ApiService apiService = ReTroFitClient.getRetrofit().create(ApiService.class);

        retrofit2.Call<List<Model>> call = apiService.getModel();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                if (response.isSuccessful()) {
                    List<Model> models = response.body();
                    // Process the list of models here
//                    String[] oneHero = new String[models.size()];
//                    for (int i = 0; i< models.size(); i++){
//                        oneHero[i] = models.get(i).getName();
//                    }
//                    tv.setAdapter(new ArrayAdapter<>(UserRetrofit.this, android.R.layout.simple_list_item_1,oneHero));

                    tvname.setText(models.get(0).getRealname());
                    tvpublisher.setText(models.get(1).getPublisher());
//                    String tvImage = models.get(2).getImage();

                } else {
                    // Handle error
                    // You can get more information about the error from response.errorBody() or response.message()
                    System.out.println("Loi call api");
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Log.d("TAG", "onFailure: Fail to call api");

            }
        });
    }



}