package com.example.ozodjonamin.safiabusiness.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ozodjonamin.safiabusiness.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    ImageView imgCategory;
    TextView txtCategory;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        imgCategory = (ImageView) itemView.findViewById(R.id.image_category);
        txtCategory = (TextView) itemView.findViewById(R.id.txt_category_name);
    }
}
