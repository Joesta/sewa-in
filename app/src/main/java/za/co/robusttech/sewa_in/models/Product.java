package za.co.robusttech.sewa_in.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.robusttech.sewa_in.enums.ItemAvailability;

/**
 * Project Name - sewa-in
 * Created on 2021/02/13 at 9:18 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable {
    private String productId;
    private String imageUrl;
    private String description;
    private String productName;
    private int quantity;
    private double price;
    private double rating;
    private double discount;
    private ItemAvailability itemAvailability;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("imageUrl", imageUrl);
        map.put("description", description);
        map.put("productName", productName);
        map.put("quantity", quantity);
        map.put("rating", rating);
        map.put("discount", discount);
        map.put("itemAvailability", itemAvailability);

        return map;

    }
}
