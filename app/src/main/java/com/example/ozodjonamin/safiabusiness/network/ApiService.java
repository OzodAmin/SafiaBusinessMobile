package com.example.ozodjonamin.safiabusiness.network;

import com.example.ozodjonamin.safiabusiness.model.Category;
import com.example.ozodjonamin.safiabusiness.model.Product;
import com.example.ozodjonamin.safiabusiness.model.Token;
import com.example.ozodjonamin.safiabusiness.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Call<Token> login(@Field("username") String username, @Field("password") String password);

    @POST("refresh")
    @FormUrlEncoded
    Call<Token> refresh(@Field("refresh_token") String refreshToken);

    @GET("user")
    Call<User> getUserInformation();

    @POST("categories")
    @FormUrlEncoded
    Call<List<Category>> categories(@Field("lang") String lang);

    @POST("productsCat")
    @FormUrlEncoded
    Call<List<Product>> products(@Field("cat_id") String catId, @Field("lang") String lang);
}
