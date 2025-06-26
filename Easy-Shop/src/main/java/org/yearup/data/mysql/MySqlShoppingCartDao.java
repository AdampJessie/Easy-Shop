package org.yearup.data.mysql;

import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {


    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();
        String sql = "SELECT sc.product_id, sc.quantity, p.* " +
                "FROM shopping_cart sc " +
                "JOIN products p ON sc.product_id = p.product_id " +
                "WHERE sc.user_id = ?;";

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
    public ShoppingCartItem addToCart(int userId, int productID) {
        return null;
    }

    @Override
    public ShoppingCartItem updateCartItem(int userId, int productID, ShoppingCartItem item) {
        return null;
    }

    @Override
    public void clearCart(int userId) {

    }

    private ShoppingCartItem mapShoppingCartItem(ResultSet row) throws SQLException {
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int productId = row.getInt("product_id");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String color = row.getString("color");
        int stock = row.getInt("stock");
        boolean featured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");
        int quantity = row.getInt("quantity");

        Product product = new Product() {{
            setName(name); setPrice(price); setCategoryId(categoryId);
            setDescription (description); setColor(color); setStock(stock);
            setFeatured(featured); setImageUrl(imageUrl); setProductId(productId);

        }};


        return new ShoppingCartItem() {{
            setProduct(product);
            setQuantity(quantity);
        }};
    }

}
