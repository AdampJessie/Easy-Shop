package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao {

    ShoppingCart getByUserId(int userId);
    ShoppingCart addToCart(int userId, int productID);
    void updateCartItem(int userId, int productID, int quantity);
    void clearCart(int userId);

}
