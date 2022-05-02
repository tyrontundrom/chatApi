import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class User {
    private String username;
    private String chatname;

    public User(String username) {
        this.username = username;
    }
}
