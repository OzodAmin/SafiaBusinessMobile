package com.example.ozodjonamin.safiabusiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ozodjonamin.safiabusiness.R;
import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;
import com.example.ozodjonamin.safiabusiness.entities.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {
        Picasso.with(context)
                .load(cartList.get(position).productImage)
                .into(holder.imgProduct);

        String price = cartList.get(position).price + " Cум / " + cartList.get(position).productMeasure;
        holder.countProduct.setNumber(String.valueOf(cartList.get(position).amount));
        holder.nameProduct.setText(cartList.get(position).productName);
        holder.priceProduct.setText(price);

        holder.countProduct.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = cartList.get(position);
                cart.amount = newValue;

                Common.cartRepository.updateCart(cart);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView nameProduct, priceProduct;
        ElegantNumberButton countProduct;

        public CartViewHolder(View itemView) {
            super(itemView);

            imgProduct = (ImageView) itemView.findViewById(R.id.img_product);
            nameProduct = (TextView) itemView.findViewById(R.id.txt_product_name);
            priceProduct = (TextView) itemView.findViewById(R.id.txt_product_price);
            countProduct = (ElegantNumberButton) itemView.findViewById(R.id.txt_product_count);
        }
    }
}
