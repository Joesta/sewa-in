package za.co.robusttech.sewa_in.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.robusttech.sewa_in.constants.IProductConstants;

/**
 * Project Name - sewa-in
 * Created on 2021/02/13 at 9:18 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product implements Serializable, IProductConstants {
    private String productId;
    private String productImageUrl;
    private String productDescription;
    private String productName;
    private int productQuantity;
    private double productPrice;
    private double productRating;
    private double productDiscount;
    private String productCategory;
    //private ProductAvailability productAvailability;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(PRODUCT_ID, productId);
        //map.put(PRODUCT_IMAGE_URL, productImageUrl);
        map.put(PRODUCT_DESCRIPTION, productDescription);
        map.put(PRODUCT_NAME, productName);
        map.put(PRODUCT_QUANTITY, productQuantity);
        map.put(PRODUCT_RATING, productRating);
        map.put(PRODUCT_DISCOUNT, productDiscount);
        //map.put(PRODUCT_AVAILABILITY, productAvailability);

        return map;
    }
}
