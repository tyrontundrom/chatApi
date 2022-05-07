import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@ToString
class User implements UserInterface{
    private String username;
    private Socket socket;
    private Map<String, Socket> userlist = new ConcurrentHashMap<>();

    public User(String username, Socket socket) {
        this.username = username;
        this.socket = socket;
    }

    @Override
    public void addUser() {
        userlist.put(username,socket);
    }

    @Override
    public Socket getSocket(String username) {
        return userlist.get(username);
    }
}
