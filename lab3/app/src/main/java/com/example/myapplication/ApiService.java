package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("save_image") // Đặt đường dẫn API lưu ảnh vào server ở đây
    Call<Void> saveImage(@Body ImageData imageData);
}
