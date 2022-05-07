import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class UsersList implements UsersListInterface {
    private String name;
    private Socket socket;
    private Map<String, Socket> userlist = new ConcurrentHashMap<>();

    @Override
    public void addUser(String name, Socket socket) {
        userlist.put(name,socket);
    }

    @Override
    public Socket getSocket(String name) {
        return userlist.get(name);
    }
}
