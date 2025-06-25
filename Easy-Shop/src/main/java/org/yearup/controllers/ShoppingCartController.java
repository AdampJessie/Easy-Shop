package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions

@RestController
@RequestMapping("/cart")
@PreAuthorize("isAuthenticated()")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @GetMapping("")
    public ShoppingCart getCart(Principal principal) {
        try {
            int userId = getUserId(principal);
            return shoppingCartDao.getByUserId(userId);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("products/{id}")
    public ShoppingCartItem addToCart(Principal principal, @PathVariable int productID) {
        try {
            int userId = getUserId(principal);
            return shoppingCartDao.addToCart(userId, productID);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }

    }

    @PutMapping("products/{id}")
    public ShoppingCartItem updateCartItem(Principal principal, @PathVariable int productID, @RequestBody ShoppingCartItem item) {
        try {
            int userId = getUser(principal).getId();

            return shoppingCartDao.updateCartItem(userId, productID, item);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @DeleteMapping("")
    public void clearCart(Principal principal) {
        try {
            int userId = getUserId(principal);

            shoppingCartDao.clearCart(userId);
        }
        catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    public int getUserId(Principal principal) {
        try {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
             User user = userDao.getByUserName(userName);
            return user.getId();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}