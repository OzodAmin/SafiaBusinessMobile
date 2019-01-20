package com.example.ozodjonamin.safiabusiness.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ozodjonamin.safiabusiness.R;
import com.example.ozodjonamin.safiabusiness.entities.IItemClickListener;

public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imgProduct;
    TextView txtName, txtPrice;
    IItemClickListener itemClickListener;

    public RelativeLayout viewBackground;
    public LinearLayout viewForeground;

    public FavouriteViewHolder(View itemView) {
        super(itemView);

        imgProduct = (ImageView) itemView.findViewById(R.id.img_fv_product);
        txtName = (TextView) itemView.findViewById(R.id.txt_fv_product_name);
        txtPrice = (TextView) itemView.findViewById(R.id.txt_fv_product_price);

        viewBackground = (RelativeLayout) itemView.findViewById(R.id.delete_background);
        viewForeground = (LinearLayout) itemView.findViewById(R.id.view_foreground);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(IItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}