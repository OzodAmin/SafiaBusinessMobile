package com.example.ozodjonamin.safiabusiness;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.ozodjonamin.safiabusiness.adapter.FavouriteAdapter;
import com.example.ozodjonamin.safiabusiness.adapter.FavouriteViewHolder;
import com.example.ozodjonamin.safiabusiness.entities.RecyclerItemTouchHelper;
import com.example.ozodjonamin.safiabusiness.entities.RecyclerItemTouchHelperListener;
import com.example.ozodjonamin.safiabusiness.manager.TokenManager;
import com.example.ozodjonamin.safiabusiness.model.Favourites;
import com.example.ozodjonamin.safiabusiness.model.Product;
import com.example.ozodjonamin.safiabusiness.network.ApiService;
import com.example.ozodjonamin.safiabusiness.network.RetrofitBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteListActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final String TAG = "FavouriteListActivity";
    String language;

    RecyclerView favouriteListRecycler;
    FavouriteAdapter adapter;
    ApiService service;
    private Call<List<Product>> call;
    private Call<Favourites> addOrRemoveCall;
    List<Product> localFavouriteList;
    TokenManager tokenManager;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        if (tokenManager.getToken() == null) {
            startActivity(new Intent(FavouriteListActivity.this, LoginActivity.class));
            finish();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);

        favouriteListRecycler = (RecyclerView) findViewById(R.id.recycler_favourite_list);
        favouriteListRecycler.setLayoutManager(new LinearLayoutManager(this));
        favouriteListRecycler.setHasFixedSize(true);

        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        language = prefs.getString("App_Lang", "ru");

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(favouriteListRecycler);

        loadFavouriteList();
    }

    private void loadFavouriteList(){
        call = service.getFavorites(language);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.w(TAG, "onResponse: " + response);
                displayFavouriteList(response.body());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void displayFavouriteList(List<Product> list) {
        localFavouriteList = list;
        adapter = new FavouriteAdapter(this, list);
        favouriteListRecycler.setAdapter(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadFavouriteList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        call.cancel();
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof FavouriteViewHolder){

            final int deletedProductIndex = viewHolder.getAdapterPosition();
            final Product deletedProduct = localFavouriteList.get(viewHolder.getAdapterPosition());

            addOrRemoveCall = service.removeProductFromFavourite(deletedProduct.id);
            addOrRemoveCall.enqueue(new Callback<Favourites>() {
                @Override
                public void onResponse(Call<Favourites> call, Response<Favourites> response) {

                    adapter.removeProduct(deletedProductIndex);

                    Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(deletedProduct.title).append(" removed from favourite list").toString(), Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            addOrRemoveCall = service.addProductToFavourite(deletedProduct.id);
                            addOrRemoveCall.enqueue(new Callback<Favourites>() {
                                @Override
                                public void onResponse(Call<Favourites> call, Response<Favourites> response) {
                                    adapter.restoreProduct(deletedProduct, deletedProductIndex);
                                }

                                @Override
                                public void onFailure(Call<Favourites> call, Throwable t) {
                                    Log.w(TAG, "onFailure (addToFavourite): " + t.getMessage());
                                }
                            });
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
                @Override
                public void onFailure(Call<Favourites> call, Throwable t) {
                    Log.w(TAG, "onFailure(deleteFromFavourite): " + t.getMessage());
                }
            });
        }
    }
}
