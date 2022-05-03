import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

@Slf4j
class ChatServer {
    private final ChatServerFactory factory = new DefaultChatServerFactory();
    private final ChatWorkers chatWorkers = factory.createChatWorkers();
    private final ExecutorService executorService = factory.createExecutorService();
    private static final int PORT = 8080;

    public static void main(String[] args) {
        new ChatServer().start(PORT);
    }

    private void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            listen(serverSocket, port);
        } catch (IOException e) {
            log.error("Server failed to start: " + e.getMessage());
        }
    }

    private void listen(ServerSocket serverSocket, int port) throws IOException {
        log.info("Server is listening on port: " + port);
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            log.info("New connection established...");
            ChatWorker chatWorker = new ChatWorker(socket, chatWorkers);
            log.info("new " + chatWorker.getClass().getName());
            chatWorkers.add(chatWorker);
            executorService.execute(chatWorker);

        }
    }
}
