package com.example.ozodjonamin.safiabusiness.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ozodjonamin.safiabusiness.R;
import com.example.ozodjonamin.safiabusiness.entities.IItemClickListener;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imgCategory;
    TextView txtCategory;

    IItemClickListener iItemClickListener;

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }

    public CategoryViewHolder(View itemView) {
        super(itemView);

        imgCategory = (ImageView) itemView.findViewById(R.id.image_category);
        txtCategory = (TextView) itemView.findViewById(R.id.txt_category_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iItemClickListener.onClick(v);
    }
}
