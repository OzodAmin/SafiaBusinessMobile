package com.example.ozodjonamin.safiabusiness.network;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.example.ozodjonamin.safiabusiness.model.Category;
import com.example.ozodjonamin.safiabusiness.model.Token;
import com.example.ozodjonamin.safiabusiness.entities.PostResponse;
import com.example.ozodjonamin.safiabusiness.model.User;

import java.util.List;

public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Call<Token> login(@Field("username") String username, @Field("password") String password);

    @POST("refresh")
    @FormUrlEncoded
    Call<Token> refresh(@Field("refresh_token") String refreshToken);

    @GET("user")
    Call<User> getUserInformation();

    @GET("posts")
    Call<PostResponse> posts();

    @GET("categories")
    Call<List<Category>> categories();
}
