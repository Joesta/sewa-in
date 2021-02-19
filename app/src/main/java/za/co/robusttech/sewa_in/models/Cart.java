package za.co.robusttech.sewa_in.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project Name - sewa-in
 * Created on 2021/02/13 at 9:20 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private String id;
    private String customerId;
    private double totalAmountDue;
    private List<Product> products;

    public double add(Product product) {
        initProductList();
        products.add(product);
        totalAmountDue += product.getPrice();
        return totalAmountDue;
    }

    public double remove(Product product) {
        initProductList();
        products.add(product);
        return totalAmountDue -= product.getPrice();
    }

    private void initProductList() {
        if (products == null) products = new ArrayList<>();
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("customerId", customerId);
        map.put("totalAmountDue", totalAmountDue);
        map.put("products", products);

        return map;
    }

}
