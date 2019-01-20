package com.example.ozodjonamin.safiabusiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ozodjonamin.safiabusiness.R;
import com.example.ozodjonamin.safiabusiness.entities.Common;
import com.example.ozodjonamin.safiabusiness.entities.IItemClickListener;
import com.example.ozodjonamin.safiabusiness.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteViewHolder> {

    private Context context;
    private List<Product> productList;

    public FavouriteAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.favourite_item_layout,null);
        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, final int position) {

        String price = productList.get(position).price + " Cум / " + productList.get(position).measure;
        holder.txtPrice.setText(price);
        holder.txtName.setText(productList.get(position).title);

        Picasso.with(context)
                .load(Common.PRODUCT_THUMB_IMAGE_URL + productList.get(position).image)
                .into(holder.imgProduct);

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

//        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "Clicked remove", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    //Create new cart
//                    Cart cartProduct = new Cart();
//                    cartProduct.productName = holder.txtName.getText().toString();
//                    cartProduct.product_id = productList.get(position).id;
//                    cartProduct.price = productList.get(position).price;
//                    cartProduct.amount = 1;
//                    cartProduct.productMeasure = productList.get(position).measure;
//                    cartProduct.productImage = Common.PRODUCT_THUMB_IMAGE_URL + productList.get(position).image;
//
//                    //Add to Cart table
//                    Common.cartRepository.insertToCart(cartProduct);
//
//                    Log.w("Cart insert", new Gson().toJson(cartProduct));
//                    Toast.makeText(context, "Product successfully added to cart", Toast.LENGTH_SHORT).show();
//                }catch (Exception ex){
//                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    public void removeProduct(int position){
        productList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreProduct(Product product, int position) {
        productList.add(position, product);
        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
