package org.yearup.data.mysql;

import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    private final MySqlProductDao productDao;

    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource, MySqlProductDao productDao) {
        super(dataSource);
        this.productDao = productDao;
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();
        String sql = "SELECT *" +
                " FROM shopping_cart" +
                " WHERE user_id = ?;";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet row = statement.executeQuery();

            while (row.next()) {
                ShoppingCartItem item = mapShoppingCartItem(row);
                cart.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cart;
    }

    @Override
    public ShoppingCart addToCart(int userId, int productID) {

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM shopping_cart WHERE user_id = ? AND product_ID = ?"
            );
            statement.setInt(1, userId);
            statement.setInt(2, productID);
            ResultSet row = statement.executeQuery();

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(productDao.getById(productID));

            if (row.next()) {

                int quantity = row.getInt("quantity");

                item.setQuantity(quantity + 1);
                updateCartItem(userId, productID, quantity + 1);

            } else {
                statement = connection.prepareStatement(
                        "INSERT INTO shopping_cart(user_id, product_id, quantity) VALUES (?, ?, ?);");
                statement.setInt(1, userId);
                statement.setInt(2, productID);
                statement.setInt(3, 1);

                item.setQuantity(1);
                statement.executeUpdate();
            }
            return getByUserId(userId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCartItem(int userId, int productID, int quantity) {
        String sql = "UPDATE shopping_cart" +
                " SET quantity = ? " +
                " WHERE product_id = ? AND user_id = ?;";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, productID);
            statement.setInt(3, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void clearCart(int userId) {

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM shopping_cart WHERE user_id = ?;");
            statement.setInt(1, userId);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ShoppingCartItem mapShoppingCartItem(ResultSet row) throws SQLException {
        int productId = row.getInt("product_id");
        int quantity = row.getInt("quantity");

        ShoppingCartItem item = new ShoppingCartItem();
        item.setProduct(productDao.getById(productId));
        item.setQuantity(quantity);
        return item;
    }

}
