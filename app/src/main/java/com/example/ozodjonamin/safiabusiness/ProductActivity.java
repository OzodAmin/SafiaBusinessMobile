package com.example.ozodjonamin.safiabusiness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.ozodjonamin.safiabusiness.adapter.ProductAdapter;
import com.example.ozodjonamin.safiabusiness.entities.Common;
import com.example.ozodjonamin.safiabusiness.manager.TokenManager;
import com.example.ozodjonamin.safiabusiness.model.Product;
import com.example.ozodjonamin.safiabusiness.network.ApiService;
import com.example.ozodjonamin.safiabusiness.network.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private static final String TAG = "ProductActivity";
    TextView headerCategoryName;

    ApiService service;
    RecyclerView listProducts;
    Call<List<Product>> call;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(ProductActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        headerCategoryName = (TextView) findViewById(R.id.txt_category_name);

        listProducts = (RecyclerView) findViewById(R.id.recycler_products);
        listProducts.setLayoutManager(new GridLayoutManager(this, 2));
        listProducts.setHasFixedSize(true);

        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("App_Lang", "ru");

        call = service.products(Common.currentCategory.getId(), language);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.w(TAG, "onResponse: " + response);
                displayProductList(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void displayProductList(List<Product> products) {
        ProductAdapter adapter = new ProductAdapter(this, products);
        listProducts.setAdapter(adapter);
    }
}