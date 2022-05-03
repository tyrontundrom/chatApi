import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
class User {
    private String username;
    private String chatname;

    public User(String username) {
        this.username = username;
    }
}
