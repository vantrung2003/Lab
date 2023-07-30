package com.example.thuchanhbuoi1.api;

import com.example.thuchanhbuoi1.model.Model;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ReTroFitClient {
   public static final String BASE_URL = "https://simplifiedcoding.net/";
   private  static Retrofit retrofit = null;



   public  static  Retrofit getRetrofit(){
       if (retrofit == null){
           Gson gson = new GsonBuilder()
                   .setDateFormat("yyyy-MM-dd HH:mm:ss")
                   .create();
           retrofit = new Retrofit.Builder()
                   .baseUrl(BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create(gson))
                   .build();
       }
       return retrofit;
   }






}


