package com.example.ozodjonamin.safiabusiness.entities;

import com.example.ozodjonamin.safiabusiness.model.User;
import com.example.ozodjonamin.safiabusiness.network.ApiService;

import retrofit2.Retrofit;

public class Common {

    // URL FOR EMULATOR
//    public static final String BASE_URL = "http://10.0.2.2:8080/api/";
//    public static final String CATEGORY_IMAGE_URL = "http://10.0.2.2:8080/uploads/category/";

    // URL FOR MOBILE
    public static final String BASE_URL = "http://192.168.1.103:8080/api/";
    public static final String CATEGORY_IMAGE_URL = "http://192.168.1.103:8080/uploads/category/";

//    private static Retrofit retrofit = null;
//
//    private static Retrofit getClient(String baseUrl){
//        if (retrofit == null){
//            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
//                    .addConverterFactory(G)
//        }
//    }
//    public static ApiService getApi(){
//        return Retrofit
//    }
}
