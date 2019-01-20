package com.example.ozodjonamin.safiabusiness.adapter;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ozodjonamin.safiabusiness.R;
import com.example.ozodjonamin.safiabusiness.entities.IItemClickListener;

public class ProductViewHolder extends ViewHolder implements View.OnClickListener{

    ImageView imgProduct, btnAddToCart, btnAddToFavourite;
    TextView txtName, txtPrice;
    IItemClickListener itemClickListener;

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductViewHolder(View itemView) {
        super(itemView);

        imgProduct = (ImageView) itemView.findViewById(R.id.image_product);
        txtName = (TextView) itemView.findViewById(R.id.txt_product_name);
        txtPrice = (TextView) itemView.findViewById(R.id.txt_product_price);
        btnAddToCart = (ImageView) itemView.findViewById(R.id.btn_add_cart);
        btnAddToFavourite = (ImageView) itemView.findViewById(R.id.btn_add_favorite);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}
