package com.example.ozodjonamin.safiabusiness.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ozodjonamin.safiabusiness.R;
import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;
import com.example.ozodjonamin.safiabusiness.entities.Common;
import com.example.ozodjonamin.safiabusiness.entities.IItemClickListener;
import com.example.ozodjonamin.safiabusiness.manager.TokenManager;
import com.example.ozodjonamin.safiabusiness.model.Favourites;
import com.example.ozodjonamin.safiabusiness.model.Product;
import com.example.ozodjonamin.safiabusiness.network.ApiService;
import com.example.ozodjonamin.safiabusiness.network.RetrofitBuilder;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    private Call<Favourites> addOrRemoveCall;
    private TokenManager tokenManager;
    private ApiService service;

    private static final String TAG = "ProductAdapter";

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_item_layout, null);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {

        String price = productList.get(position).price + " Cум / " + productList.get(position).measure;
        holder.txtPrice.setText(price);
        holder.txtName.setText(productList.get(position).title);

        Picasso.with(context)
                .load(Common.PRODUCT_IMAGE_URL + productList.get(position).image)
                .into(holder.imgProduct);

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        //Favourite images
        for (Favourites temp : Common.favouritesList) {
            if (temp.getId() == productList.get(position).id) {
                holder.btnAddToFavourite.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                holder.btnAddToFavourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
        }

        holder.btnAddToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenManager = Common.tokenManager;
                service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
                addOrRemoveCall = service.addProductToFavourite(productList.get(position).id);
                addOrRemoveCall.enqueue(new Callback<Favourites>() {
                    @Override
                    public void onResponse(Call<Favourites> call, Response<Favourites> response) {
                        Log.w(TAG, "onResponse: " + response);
                        if (response.body().getId() == Common.PRODUCT_DELETED) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Remove product");
                            builder.setMessage("Do you want to remove product from favourites?");

                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addOrRemoveCall = service.removeProductFromFavourite(productList.get(position).id);
                                    addOrRemoveCall.enqueue(new Callback<Favourites>() {
                                        @Override
                                        public void onResponse(Call<Favourites> call, Response<Favourites> response) {
                                            holder.btnAddToFavourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                            Toast.makeText(context, "Product removed from favorite list", Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onFailure(Call<Favourites> call, Throwable t) {
                                            Log.w(TAG, "onFailure: " + t.getMessage());
                                        }
                                    });
                                }
                            });

                            builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();

                        } else {
                            holder.btnAddToFavourite.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(context, "Product added to favorite list", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Favourites> call, Throwable t) {
                        Log.w(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Create new cart
                    Cart cartProduct = new Cart();
                    cartProduct.productName = holder.txtName.getText().toString();
                    cartProduct.product_id = productList.get(position).id;
                    cartProduct.price = productList.get(position).price;
                    cartProduct.amount = 1;
                    cartProduct.productMeasure = productList.get(position).measure;
                    cartProduct.productImage = Common.PRODUCT_THUMB_IMAGE_URL + productList.get(position).image;

                    //Add to Cart table
                    Common.cartRepository.insertToCart(cartProduct);

                    Log.w("Cart insert", new Gson().toJson(cartProduct));
                    Toast.makeText(context, "Product successfully added to cart", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
