package com.example.ozodjonamin.safiabusiness.database.dataSource;

import com.example.ozodjonamin.safiabusiness.database.modelDatabase.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource {

    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource){
        if (instance == null)
            instance = new CartRepository(iCartDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartProducts() {
        return iCartDataSource.getCartProducts();
    }

    @Override
    public Flowable<List<Cart>> getCartProductsByCartId(int id) {
        return iCartDataSource.getCartProductsByCartId(id);
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public void emptyCart() {
        iCartDataSource.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }

    @Override
    public void updateCart(Cart... carts) {
        iCartDataSource.updateCart(carts);
    }

    @Override
    public void deleteCartByProduct(Cart cart) {
        iCartDataSource.deleteCartByProduct(cart);
    }
}
