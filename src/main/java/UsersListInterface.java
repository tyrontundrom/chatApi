import java.net.Socket;

public interface UsersListInterface {
    void addUser(String name, Socket socket);

    Socket getSocket(String name);
}
