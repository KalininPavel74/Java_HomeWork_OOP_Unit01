package unit1;

import java.util.logging.*;
import java.util.HashMap;
import java.util.Map;

public class Backet {

    static Logger logger = Logger.getLogger(Backet.class.getName());
    private Map<Product, Integer> order = new HashMap<Product, Integer>();
    synchronized public void addProduct(Product product, Integer qty) throws ShopException {
        for(Product p: order.keySet()) {
            if(p.getName().equals(product.getName())) {
                p.setReserve(qty);
                Integer curr_qty = order.get(p);
                order.put(p, curr_qty + qty);
                return;
            }
        }
        product.setReserve(qty);
        order.put(product, qty);
    }

    synchronized public void sale() {
        for(Product p: order.keySet()) {
            try {
                p.sale(order.get(p));
            } catch (Exception e) {
                logger.warning(e.getMessage());
                p.cancelReserve(order.get(p));
            }
        }
        order.clear();
    }

    public int size() {
        return order.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Корзина:").append("\n");
        for(Product p: order.keySet()) {
            sb.append("\t").append(order.get(p)).append(" шт. -").append(p).append("\n");
        }
        return sb.toString();
    }

}
