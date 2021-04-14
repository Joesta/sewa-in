package za.co.robusttech.sewa_in.models;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.robusttech.sewa_in.constants.ICustomerConstants;

/**
 * Project Name - sewa-in
 * Created on 2021/01/27 at 4:25 AM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String cellphoneNumber;
    private String emailAddress;
    private String password;
    private String imageUrl;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(ICustomerConstants.FIRST_NAME, firstName);
        map.put(ICustomerConstants.LAST_NAME, lastName);
        map.put(ICustomerConstants.CELLPHONE_NUMBER, cellphoneNumber);
        map.put(ICustomerConstants.EMAIL_ADDRESS, emailAddress);
        map.put(ICustomerConstants.IMAGE_URL, imageUrl);

        return map;
    }
}
