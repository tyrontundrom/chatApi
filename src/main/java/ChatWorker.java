import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Slf4j
@ToString
class ChatWorker implements Runnable {
    private static final String END_SESSION_COMMAND = "\\q";
    private static final String NEW_CHANNEL_COMMAND = "\\r";
    private static final String SEND_MESSAGE_COMMAND = "\\p";
    private static final String SAVE_MESSAGE_COMMAND = "\\s";

    private final Socket socket;
    private final ChatWorkers chatWorkers;
    private MessageWriter writer;
    private FileService fileService;


    Scanner in = new Scanner(System.in);
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
            privateChat(text);
        } else if (text.endsWith(SEND_MESSAGE_COMMAND)) {
            sendFile(text);
        } else if (text.endsWith(SAVE_MESSAGE_COMMAND)) {
            saveFile(text);
        } else {
            chatWorkers.broadcast(text);
        }
    }

    public void send(String text) {
        writer.write(text);
    }

    public void sendFile(String text) {
        fileName();
        fileService.sendFile(socket);
    }

    public void saveFile(String text) {
        writer.write(text);

        fileService.saveFile();
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Closing socked failed: " + e.getMessage());
        }
    }

    private void privateChat(String text) {
        User user = ChatClient.getUser();
    }

    private void fileName() {
        System.out.println("nazwa pliku");
        String fileName = in.nextLine();
        fileService = new FileService(fileName);
    }
}

