package unit1;

import java.util.logging.*;

public class Product {

    static Logger logger = Logger.getLogger(Product.class.getName());

    private String name;
    private float price = 0;
    private int rating = 0;
    private int qty = 0;
    private int reserve = 0;

    public Product(String name, float price, int rating, int qty) throws UserException {
        if(name == null || name.isBlank())
            throw new UserException("Наименование товара не может быть пустым.");
        this.name = name;
        this.price = (price<0)?0:price;
        this.rating = (rating<0 || rating>9)?0:rating;
        this.qty = (qty<0)?0:qty;
    }

    synchronized public void setQty(int qty) {
        this.qty = qty;
    }

    synchronized public void sale(int qty) throws ShopException {
        if(this.reserve - qty < 0)
            throw new ShopException("Продажа не прошла. Товара меньше, чем требуется.");
        this.reserve -= qty;
    }

    synchronized public void setReserve(int qty) throws ShopException {
        if(this.qty - qty < 0)
            throw new ShopException("Резерв не прошел. Товара меньше, чем требуется.");
        this.qty -= qty;
        this.reserve += qty;
    }

    synchronized public void cancelReserve(int qty) {
        if(this.reserve - qty < 0) {
            this.qty += this.reserve;
            this.reserve = 0;
        } else {
            this.qty += qty;
            this.reserve -= qty;
        }
    }

    public String getName() {
        return this.name;
    }

    public float getPrice() {
        return this.price;
    }

    public int getRating() {
        return this.rating;
    }

    public int getQty() {
        return this.qty;
    }

    public int getReserve() {
        return this.reserve;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("Товар: %s, кол-во: %d, зарезервированно: %d (цена: %f, рейтинг: %d)"
                ,name,qty,reserve,price,rating);
    }
}
