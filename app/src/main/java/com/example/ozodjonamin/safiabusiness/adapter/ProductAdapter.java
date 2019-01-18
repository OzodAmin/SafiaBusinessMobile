package com.example.ozodjonamin.safiabusiness.adapter;

import android.content.Context;
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
import com.example.ozodjonamin.safiabusiness.model.Product;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    Context context;
    List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.product_item_layout,null);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {

        holder.txtPrice.setText(new StringBuilder("$ ").append(productList.get(position).price).toString());
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
                    cartProduct.productImage = Common.PRODUCT_THUMB_IMAGE_URL + productList.get(position).image;

                    //Add to Cart table
                    Common.cartRepository.insertToCart(cartProduct);

                    Log.w("Cart insert", new Gson().toJson(cartProduct));
                    Toast.makeText(context, "Product successfully added to cart", Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
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
