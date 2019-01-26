package com.example.ozodjonamin.safiabusiness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.example.ozodjonamin.safiabusiness.adapter.ProductAdapter;
import com.example.ozodjonamin.safiabusiness.manager.TokenManager;
import com.example.ozodjonamin.safiabusiness.model.Product;
import com.example.ozodjonamin.safiabusiness.network.ApiService;
import com.example.ozodjonamin.safiabusiness.network.RetrofitBuilder;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    List<String> suggestList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    MaterialSearchBar searchBar;
    RecyclerView searchRecycler;
    ApiService service;
    Call<List<Product>> call;
    TokenManager tokenManager;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(SearchActivity.this, LoginActivity.class));
            finish();
        }

        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.setHint("Enter product name");

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        searchRecycler = (RecyclerView) findViewById(R.id.recycler_search);
        searchRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        searchRecycler.setHasFixedSize(true);

        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("App_Lang", "ru");

        call = service.getAllProducts(language);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.w(TAG, "onResponse: " + response);
                displayProductList(response.body());
                buildSuggestList(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });

        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for (String search : suggestList){
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    searchRecycler.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        List<Product> result = new ArrayList<>();
        for (Product product : productList)
            if (product.title.contains(text))
                result.add(product);
        adapter = new ProductAdapter(this, result);
        searchRecycler.setAdapter(adapter);
    }

    private void buildSuggestList(List<Product> list) {
        for (Product product : list)
            suggestList.add(product.title);
        searchBar.setLastSuggestions(suggestList);
    }

    private void displayProductList(List<Product> products) {
        productList = products;
        adapter = new ProductAdapter(this, products);
        searchRecycler.setAdapter(adapter);
    }
}

