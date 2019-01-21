package com.example.ozodjonamin.safiabusiness;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.ozodjonamin.safiabusiness.adapter.CartAdapter;
import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;
import com.example.ozodjonamin.safiabusiness.entities.Common;
import com.example.ozodjonamin.safiabusiness.entities.RecyclerItemTouchHelper;
import com.example.ozodjonamin.safiabusiness.entities.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView cartRecycler;
    @BindView(R.id.btn_place_order)
    Button btnPlaceOrder;

    CompositeDisposable compositeDisposable;
    RelativeLayout rootLayout;
    List<Cart> cartList = new ArrayList<>();
    CartAdapter cartAdapter;

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

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(cartRecycler);

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
        cartList = carts;
        cartAdapter = new CartAdapter(this, carts);
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadCartItems();
    }

    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof CartAdapter.CartViewHolder) {

            final int deletedProductIndex = viewHolder.getAdapterPosition();
            final Cart deletedProduct = cartList.get(viewHolder.getAdapterPosition());

            cartAdapter.removeProduct(deletedProductIndex);
            Common.cartRepository.deleteCartByProduct(deletedProduct);

            Snackbar snackbar = Snackbar.make(rootLayout, new StringBuilder(deletedProduct.productName).append(" removed from favourite list").toString(), Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreProduct(deletedProduct, deletedProductIndex);
                    Common.cartRepository.insertToCart(deletedProduct);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
