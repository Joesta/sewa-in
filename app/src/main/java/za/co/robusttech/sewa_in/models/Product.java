package za.co.robusttech.sewa_in.models;

import java.io.Serializable;

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
    private int quantity;
    private double price;
    private double rating;
    private ItemAvailability itemAvailability;
}
