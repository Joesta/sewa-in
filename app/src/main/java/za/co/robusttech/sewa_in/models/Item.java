package za.co.robusttech.sewa_in.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.robusttech.sewa_in.enums.ItemAvailability;

/**
 * Project Name - sewa-in
 * Created on 2021/01/27 at 4:29 AM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String itemId;
    private String imageUrl;
    private int quantity;
    private double price;
    private double rating;
    private ItemAvailability itemAvailability;
}
