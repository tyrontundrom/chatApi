import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class ListChatWorkers  implements ChatWorkers {
    private final List<ChatWorker> chatWorkers = new ArrayList<>();

    @Override
    public void add(ChatWorker chatWorker) {
        chatWorkers.add(chatWorker);
    }

    @Override
    public void remove(ChatWorker chatWorker) {
        chatWorkers.remove(chatWorker);
    }

    @Override
    public void broadcast(String text) {
        chatWorkers.forEach(chatWorker -> chatWorker.send(text));
    }

    @Override
    public void broadcastChannel(String text, String chatName) {
        chatWorkers.stream().filter(chatWorker -> chatWorker.equals(chatName)).forEach(chatWorker -> chatWorker.send(text));
    }
}
