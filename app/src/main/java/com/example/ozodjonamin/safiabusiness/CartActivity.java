package com.example.ozodjonamin.safiabusiness;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ozodjonamin.safiabusiness.adapter.CartAdapter;
import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;
import com.example.ozodjonamin.safiabusiness.entities.Common;
import com.example.ozodjonamin.safiabusiness.entities.RecyclerItemTouchHelper;
import com.example.ozodjonamin.safiabusiness.entities.RecyclerItemTouchHelperListener;
import com.example.ozodjonamin.safiabusiness.manager.UserManager;
import com.example.ozodjonamin.safiabusiness.network.ApiService;
import com.example.ozodjonamin.safiabusiness.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final String TAG = "CartActivity";

    RecyclerView cartRecycler;
    Button btnPlaceOrder;
    RadioGroup radioGroup;
    EditText edtNote;
    TextView txtTotalPrice, txtdiscount, txttotalWithDiscount;

    CompositeDisposable compositeDisposable;
    RelativeLayout rootLayout;
    List<Cart> cartList = new ArrayList<>();
    List<Map<String, Integer>> products;

    CartAdapter cartAdapter;
    UserManager userManager;

    ApiService service;
    Call<String> callSubmit;

    String prefferedTime = "";
    int totalPrice, discount, discountedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ButterKnife.bind(this);
        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, Common.tokenManager);

        compositeDisposable = new CompositeDisposable();
        userManager = UserManager.getInstance(getSharedPreferences("user", MODE_PRIVATE));

        cartRecycler = (RecyclerView) findViewById(R.id.recycler_cart);
        cartRecycler.setLayoutManager(new LinearLayoutManager(this));
        cartRecycler.setHasFixedSize(true);

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(cartRecycler);

        loadCartItems();

        btnPlaceOrder = (Button) findViewById(R.id.btn_place_order);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    private void placeOrder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Submit order");

        View submitOrderLayout = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);
        edtNote = (EditText) submitOrderLayout.findViewById(R.id.edt_note);

        txtTotalPrice = (TextView) submitOrderLayout.findViewById(R.id.total_price);
        txtdiscount = (TextView) submitOrderLayout.findViewById(R.id.discount);
        txttotalWithDiscount = (TextView) submitOrderLayout.findViewById(R.id.total_price_with_discount);

        totalPrice = 0;

        products = new ArrayList<>();

        for (Cart cart : cartList) {
            totalPrice += cart.price * cart.amount;
            Map<String, Integer> product = new HashMap<>();
            product.put("product_id", cart.product_id);
            product.put("quantity", cart.amount);
            products.add(product);
        }

        discount = userManager.getUserInformation().getDiscount();
        discountedPrice = (int) (totalPrice * (discount / 100.0f));

        txtTotalPrice.setText(new StringBuilder(String.valueOf(totalPrice)).append(" Сум").toString());
        txtdiscount.setText(new StringBuilder(String.valueOf(discount)).append(" %").toString());
        txttotalWithDiscount.setText(new StringBuilder(String.valueOf(totalPrice - discountedPrice)).append(" Сум").toString());

        radioGroup = (RadioGroup) submitOrderLayout.findViewById(R.id.radio_group);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbtn1:
                        prefferedTime = "9:00 - 12:00";
                        break;
                    case R.id.rbtn2:
                        prefferedTime = "12:00 - 15:00";
                        break;
                    case R.id.rbtn3:
                        prefferedTime = "15:00 - 18:00";
                        break;
                }
            }
        });

        builder.setView(submitOrderLayout);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submit(products);
            }
        }).show();

    }

    private void submit(List<Map<String, Integer>> products) {
        callSubmit = service.submitOrder(products, products.size(), edtNote.getText().toString(), prefferedTime, totalPrice, discount, totalPrice - discountedPrice);
        callSubmit.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.w(TAG, "onResponse: " + response);

                Intent intent = new Intent(CartActivity.this ,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Common.cartRepository.emptyCart();
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
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

        if (carts == null || carts.isEmpty())
            btnPlaceOrder.setVisibility(View.INVISIBLE);
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
