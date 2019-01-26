package com.example.ozodjonamin.safiabusiness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.ozodjonamin.safiabusiness.adapter.OrderAdapter;
import com.example.ozodjonamin.safiabusiness.manager.TokenManager;
import com.example.ozodjonamin.safiabusiness.model.Order;
import com.example.ozodjonamin.safiabusiness.network.ApiService;
import com.example.ozodjonamin.safiabusiness.network.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersListActivity extends AppCompatActivity {

    private static final String TAG = "OrdersListActivity";

    RecyclerView orderListRecycler;
    OrderAdapter adapter;
    ApiService service;
    private Call<List<Order>> call;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(OrdersListActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        orderListRecycler = (RecyclerView) findViewById(R.id.recycler_orders);
        orderListRecycler.setLayoutManager(new LinearLayoutManager(this));
        orderListRecycler.setHasFixedSize(true);

        call = service.getOrderList();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                Log.w(TAG, "onResponse: " + response);
                displayOrdersList(response.body());
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void displayOrdersList(List<Order> orderList) {
        adapter = new OrderAdapter(this, orderList);
        orderListRecycler.setAdapter(adapter);
    }
}
