public interface ChatWorkers {

    void add(ChatWorker chatWorker);

    void remove(ChatWorker chatWorker);

    void broadcast(String text);
}
