package za.co.robusttech.sewain.models;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project Name - sewa-in
 * Created on 2021/02/13 at 9:24 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private Date orderDate;
    private Date orderCollectionDate;
    private boolean isCollected;
    private List<Product> products;
    private String customerId;
}
