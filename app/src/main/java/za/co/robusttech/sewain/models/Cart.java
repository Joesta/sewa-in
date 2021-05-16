package za.co.robusttech.sewain.models;

import java.util.HashMap;
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
    private String customerId;
    private Product product;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", customerId);
        map.put("product", product);

        return map;
    }

}
