import java.util.concurrent.ExecutorService;

public interface ChatServerFactory {

    ChatWorkers createChatWorkers();

    ExecutorService createExecutorService();
}
