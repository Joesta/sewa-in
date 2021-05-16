package za.co.robusttech.sewain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Project Name - sewa-in
 * Created on 2021/05/11 at 9:44 PM
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BrainTreeToken {
    private String clientToken;
    private boolean sucdess;
}
