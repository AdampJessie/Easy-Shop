package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao {

    ShoppingCart getByUserId(int userId);
    ShoppingCartItem addToCart(int userId, int productID);
    ShoppingCartItem updateCartItem(int userId, int productID, ShoppingCartItem item);
    void clearCart(int userId);

}
