import java.io.File;
import java.net.Socket;

public interface ChatWorkers {

    void add(ChatWorker chatWorker);

    void remove(ChatWorker chatWorker);

    void broadcast(String text);

    void broadcastChannel(String text, String chatName);

    Socket getSocket();

    void sendFileWorker(File file,Socket socket);
}
