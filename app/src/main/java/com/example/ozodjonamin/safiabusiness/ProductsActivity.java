package com.example.ozodjonamin.safiabusiness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ozodjonamin.safiabusiness.Responses.ProductListResponse;
import com.example.ozodjonamin.safiabusiness.network.ApiService;
import com.example.ozodjonamin.safiabusiness.network.RetrofitBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {

    private static final String TAG = "ProductsListActivity";

    @BindView(R.id.post_title)
    TextView title;

    ApiService service;
    TokenManager tokenManager;
    Call<ProductListResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(ProductsActivity.this, LoginActivity.class));
            finish();
        }
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
    }

    @OnClick(R.id.btn_posts)
    void getPosts(){

        call = service.products();
        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){
                    title.setText(response.body().getData().get(0).getTitle());
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(ProductsActivity.this, LoginActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(call != null){
            call.cancel();
            call = null;
        }
    }
}
