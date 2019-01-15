package com.example.ozodjonamin.safiabusiness.database.local;

import com.example.ozodjonamin.safiabusiness.database.dataSource.ICartDataSource;
import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO) {
        if (instance == null)
            instance = new CartDataSource(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartProducts() {
        return cartDAO.getCartProducts();
    }

    @Override
    public Flowable<List<Cart>> getCartProductsByCartId(int id) {
        return cartDAO.getCartProductsByCartId(id);
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        cartDAO.updateCart();
    }

    @Override
    public void deleteCartByProduct(Cart cart) {
        cartDAO.deleteCartByProduct(cart);
    }
}
