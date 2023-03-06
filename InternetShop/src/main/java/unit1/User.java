package unit1;

import java.util.logging.*;
import java.util.List;
import java.util.ArrayList;

public class User {
    static Logger logger = Logger.getLogger(User.class.getName());

    static List<String> names = new ArrayList<String>();
    private String login;
    private String password;
    private Backet backet = new Backet();

    public User(String login, String password) throws UserException, ShopException {
        if(login == null || login.isBlank())
            throw new UserException("Логин пользователя не может быть пустым.");
        if(password == null || password.isBlank())
            throw new UserException("Пароль пользователя не может быть пустым.");
        if(names.indexOf(login) != -1)
            throw new ShopException("Пользователь с таким именем уже существует.");
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setPassword(String password) throws UserException {
        if(password == null || password.isBlank())
            throw new UserException("Пароль пользователя не может быть пустым.");
        this.password = password;
    }

    public boolean addProduct(Product product, Integer qty) {
        if(product.getQty()<qty) {
            logger.warning(String.format("%s: Товара (остаток %d) меньше, чем требуется (%d)"
                    ,login,product.getQty(),qty));
            return false;
        }
        try {
            backet.addProduct(product, qty);
            logger.warning(String.format("%s: В корзину добавлен %s - %d"
                    ,login,product.getName(),qty));
            return true;
        } catch (ShopException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void sale() {
        try {
            backet.sale();
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    @Override
    public String toString() {
        if(backet.size()>0)
            return login+"\n"+backet;
        else return "Корзина "+login+" пуста.\n";
    }

}
