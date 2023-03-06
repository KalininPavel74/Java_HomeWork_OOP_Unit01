package unit1;

import java.util.logging.*;
import java.util.ArrayList;
import java.util.List;

public class Category {

    static Logger logger = Logger.getLogger(Category.class.getName());

    private String name;
    private List<Product> products = new ArrayList<Product>();

    public Category(String name) throws UserException {
        if(name == null || name.isBlank())
            throw new UserException("Наименование категории не может быть пустым.");
        this.name = name;
    }

    public void addProduct(Product product) throws UserException, ShopException {
        if(product == null)
            throw new UserException("Добавляемый в категорию продукт не может быть пустым.");
        for(Product p: products)
            if(p.getName().equals(product.getName()))
                throw new ShopException("Добавляемый в категорию "+name+" продукт уже присутствует.");
        products.add(product);
    }

    public Product getProductByName(String searchName) throws Exception {
        for(Product p: products)
            if(p.getName().equals(searchName))
                return p;
        return null;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Категория: ").append(name).append("\n");
        for(Product p: products)
            sb.append("\t").append(p).append("\n");
        return sb.toString();
    }

}
