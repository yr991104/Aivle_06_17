package labcqrssummarize.domain;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginCommand {
    //id
    @NotBlank
    private String userId;
    //password
    @NotBlank
    private String password;
}
