package com.example.ozodjonamin.safiabusiness.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ozodjonamin.safiabusiness.R;
import com.example.ozodjonamin.safiabusiness.entities.IItemClickListener;
import com.example.ozodjonamin.safiabusiness.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new OrderAdapter.OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {

        holder.orderId.setText("#" + orderList.get(position).getId());
        holder.orderCreatedAt.setText(orderList.get(position).getCreated_at());
        switch (orderList.get(position).getStatus()){
            case 1:
                holder.orderStatus.setText("NEW");
                holder.itemLinearLayout.setBackgroundColor(Color.BLUE);
                break;
            case 2:
                holder.orderStatus.setText("ACCEPTED");
                holder.itemLinearLayout.setBackgroundColor(Color.GREEN);
                break;
            case 3:
                holder.orderStatus.setText("DECLINED");
                holder.itemLinearLayout.setBackgroundColor(Color.RED);
                break;
        }

        holder.setItemClickListener(new IItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView orderId, orderCreatedAt, orderStatus;

        public LinearLayout itemLinearLayout;

        IItemClickListener itemClickListener;

        public OrderViewHolder(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.order_id);
            orderCreatedAt = (TextView) itemView.findViewById(R.id.txt_order_created);
            orderStatus = (TextView) itemView.findViewById(R.id.txt_order_status);

            itemLinearLayout = (LinearLayout) itemView.findViewById(R.id.itemLinearLayout);
        }

        public void setItemClickListener(IItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v);
        }
    }
}
