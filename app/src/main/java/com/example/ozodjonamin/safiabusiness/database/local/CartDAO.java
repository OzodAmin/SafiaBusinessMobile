package com.example.ozodjonamin.safiabusiness.database.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartProducts();

    @Query("SELECT * FROM Cart WHERE id=:id")
    Flowable<List<Cart>> getCartProductsByCartId(int id);

    @Query("SELECT COUNT(*) FROM Cart")
    int countCartItems();

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert
    void insertToCart(Cart...carts);

    @Update
    void updateCart(Cart...carts);

    @Delete
    void deleteCartByProduct(Cart cart);
}
