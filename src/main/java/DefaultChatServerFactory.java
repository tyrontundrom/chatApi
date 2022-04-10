import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class DefaultChatServerFactory implements ChatServerFactory {
    @Override
    public ChatWorkers createChatWorkers() {
        return new SynchronizedChatWorkersProxy(new ListChatWorkers());
    }

    @Override
    public ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(1024);
    }
}
