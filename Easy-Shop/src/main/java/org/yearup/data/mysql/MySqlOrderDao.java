/*
package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.security.Principal;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    private final MySqlShoppingCartDao shoppingCartDao;

    @Autowired
    public MySqlOrderDao(DataSource dataSource, MySqlShoppingCartDao shoppingCartDao) {
        super(dataSource);
        this.shoppingCartDao = shoppingCartDao;
    }

    @Override
    public Order create(int userID) {
        ShoppingCart cart = shoppingCartDao.getByUserId(userID);

    }



}
*/
