import java.net.Socket;

public interface UserInterface {
    void addUser();

    Socket getSocket(String username);
}
