import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
@ToString
@Getter
class ChatWorker implements Runnable {
    private static final String END_SESSION_COMMAND = "\\q";
    private static final String NEW_CHANNEL_COMMAND = "@";
    private static final String SEND_MESSAGE_COMMAND = "\\p";
    private static final String SAVE_MESSAGE_COMMAND = "\\s";

    private final Socket socket;
    private final ChatWorkers chatWorkers;
    private MessageWriter writer;
    private FileService fileService;
    private User user;
    private String userName;
    private PrivateChat privateChat;
    private UsersListInterface listInterface;

    Scanner in = new Scanner(System.in);
    public ChatWorker(Socket socket, ChatWorkers chatWorkers,UsersListInterface listInterface) {
        this.socket = socket;
        this.chatWorkers = chatWorkers;
        writer = new MessageWriter(socket);
        this.listInterface = listInterface;
    }

    @Override
    public void run() {
        new MessageReader(socket, this::onText, () -> chatWorkers.remove(this)).read();
    }

    private void onText(String text) {
        String[] split = text.split(" ");
        String usernameToSend = split[1].substring(1);
        userName = split[0].substring(0,split[0].length()-1);
        listInterface.addUser(userName,socket);
        if (text.endsWith(END_SESSION_COMMAND)) {
            closeSocket();
        } else if (text.contains(NEW_CHANNEL_COMMAND)) {

//            chatWorkers.broadcastChannel(text, chatName);
            new MessageWriter(listInterface.getSocket(usernameToSend)).write(split[2]);
        } else if (text.endsWith(SEND_MESSAGE_COMMAND)) {
            sendFile();
        } else if (text.endsWith(SAVE_MESSAGE_COMMAND)) {
            saveFile();
        } else {
            chatWorkers.broadcast(text);
        }
    }

    public void send(String text) {
        writer.write(text);
    }

    public void sendFile() {
        String fiepath = fileName();
        File file = new File(fiepath);
        fileService = new FileService(file);
        fileService.sendFile(socket);
    }

    public void saveFile() {
        fileName();

        fileService.saveFile();
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            log.error("Closing socked failed: " + e.getMessage());
        }
    }

    public void privateChat() {

    }

    private String fileName() {
        System.out.println("nazwa pliku");
        String fileName = in.nextLine();
        return fileName;
    }

}

