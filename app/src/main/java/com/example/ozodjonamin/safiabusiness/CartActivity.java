package com.example.ozodjonamin.safiabusiness;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.ozodjonamin.safiabusiness.adapter.CartAdapter;
import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;
import com.example.ozodjonamin.safiabusiness.entities.Common;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecycler;
    @BindView(R.id.btn_place_order)
    Button btnPlaceOrder;

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        compositeDisposable = new CompositeDisposable();

        cartRecycler = (RecyclerView) findViewById(R.id.recycler_cart);
        cartRecycler.setLayoutManager(new LinearLayoutManager(this));
        cartRecycler.setHasFixedSize(true);

        loadCartItems();
    }

    private void loadCartItems() {
        compositeDisposable.add(
                Common.cartRepository.getCartProducts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCartItems(carts);
                    }
                })
        );
    }

    private void displayCartItems(List<Cart> carts) {
        CartAdapter cartAdapter = new CartAdapter(this, carts);
        cartRecycler.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
