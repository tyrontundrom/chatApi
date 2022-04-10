import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
@ToString
class ChatWorker implements Runnable {
    private static final String END_SESSION_COMMAND = "\\q";
    private static final String NEW_CHANNEL_COMMAND = "\\r";

    private final Socket socket;
    private final ChatWorkers chatWorkers;
    private MessageWriter writer;

    public ChatWorker(Socket socket, ChatWorkers chatWorkers) {
        this.socket = socket;
        this.chatWorkers = chatWorkers;
        writer = new MessageWriter(socket);
    }

    @Override
    public void run() {
        new MessageReader(socket, this::onText, () -> chatWorkers.remove(this)).read();
    }

    private void onText(String text) {
        if (text.endsWith(END_SESSION_COMMAND)) {
            closeSocket();
        } else if (text.endsWith(NEW_CHANNEL_COMMAND)) {
            new ChannelClient().start();
        } else {
            chatWorkers.broadcast(text);
        }
    }

    public void send(String text) {
        writer.write(text);
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Closing socked failed: " + e.getMessage());
        }
    }
}

