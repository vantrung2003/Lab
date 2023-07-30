package com.example.thuchanhbuoi1.api;

import com.example.thuchanhbuoi1.model.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("demos/marvel/")
    Call<List<Model>> getModel() ;

}
