package za.co.robusttech.sewa_in.models;

import java.util.List;

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
    private double totalAmountDue;
    private List<Product> products;
}
