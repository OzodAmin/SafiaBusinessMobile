package com.example.ozodjonamin.safiabusiness.database.dataSource;

import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {
    Flowable<List<Cart>> getCartProducts();
    Flowable<List<Cart>> getCartProductsByCartId(int id);
    int countCartItems();
    void emptyCart();
    void insertToCart(Cart...carts);
    void updateCart(Cart...carts);
    void deleteCartByProduct(Cart cart);
}
