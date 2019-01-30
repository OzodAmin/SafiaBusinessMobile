package com.example.ozodjonamin.safiabusiness.network;

import com.example.ozodjonamin.safiabusiness.model.Category;
import com.example.ozodjonamin.safiabusiness.model.Favourites;
import com.example.ozodjonamin.safiabusiness.model.Order;
import com.example.ozodjonamin.safiabusiness.model.Product;
import com.example.ozodjonamin.safiabusiness.model.Token;
import com.example.ozodjonamin.safiabusiness.model.User;

import java.util.List;
import java.util.Map;

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

    @POST("products")
    @FormUrlEncoded
    Call<List<Product>> getAllProducts(@Field("lang") String lang);

    @POST("getFavorites")
    @FormUrlEncoded
    Call<List<Product>> getFavorites(@Field("lang") String lang);

    @GET("favouriteProductsIds")
    Call<List<Favourites>> favouriteIds();

    @POST("addProductToFavourite")
    @FormUrlEncoded
    Call<Favourites> addProductToFavourite(@Field("product_id") int id);

    @POST("removeProductFromFavourite")
    @FormUrlEncoded
    Call<Favourites> removeProductFromFavourite(@Field("product_id") int id);

    @GET("orders")
    Call<List<Order>> getOrderList();

    @POST("addOrder")
    @FormUrlEncoded
    Call<String> submitOrder(@Field("products[]") List<Map<String, Integer>> products,
                                 @Field("products_count") int productsCount,
                                 @Field("note") String note,
                                 @Field("preferred_time") String prefered_time,
                                 @Field("total_price") int total_price,
                                 @Field("discount") int discount,
                                 @Field("total_price_with_discount") int total_price_with_discount
                                 );
}