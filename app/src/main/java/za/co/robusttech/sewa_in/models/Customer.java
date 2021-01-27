package za.co.robusttech.sewa_in.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Project Name - sewa-in
 * Created on 2021/01/27 at 4:25 AM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private String customerId;
    private String firstName;
    private String lastName;
    private String cellNumber;
    private String emailAddress;
    private String password;
}
