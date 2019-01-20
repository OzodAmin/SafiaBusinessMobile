package com.example.ozodjonamin.safiabusiness.entities;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.ozodjonamin.safiabusiness.adapter.FavouriteViewHolder;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,RecyclerItemTouchHelperListener listener ) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (listener != null)
            listener.onSwipe(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foreGround = ((FavouriteViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foreGround);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null){
            View foreGround = ((FavouriteViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onSelected(foreGround);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGround = ((FavouriteViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDraw(c, recyclerView, foreGround, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foreGround = ((FavouriteViewHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foreGround, dX, dY, actionState, isCurrentlyActive);
    }
}
