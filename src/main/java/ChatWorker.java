import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
@ToString
@Getter
class ChatWorker implements Runnable {
    private static final String END_SESSION_COMMAND = "\\q";
    private static final String NEW_CHANNEL_COMMAND = "@";
    private static final String SEND_MESSAGE_COMMAND = "#send";
    private static final String SAVE_MESSAGE_COMMAND = "#save";
    private static final String SHOW_HISTORY_COMMAND = "#show";

    private final Socket socket;
    private final ChatWorkers chatWorkers;
    private MessageWriter writer;
    private FileService fileService;
    private User user;
    private String userName;
    private PrivateChat privateChat;
    private UsersListInterface listInterface;
    private List<String> history = new ArrayList<>();

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
            new MessageWriter(listInterface.getSocket(usernameToSend)).write("priv->" + split[0] + " " + split[2]);
        } else if (text.contains(SEND_MESSAGE_COMMAND)) {
            File file = new File(split[2]);
//            fileService = new FileService(file);
//            fileService.sendFile(socket);
//            sendFile();
            chatWorkers.sendFileWorker(file, socket);
            chatWorkers.broadcast(split[0] + " has send file: " + file.getPath());
            System.out.println(file.length());
            System.out.println(file.exists());
            System.out.println(file.getName());
        } else if (text.contains(SAVE_MESSAGE_COMMAND)) {
//            saveFile(split[2]);
            fileService = new FileService();
            try {
                fileService.receiveFile(split[2],socket );
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Save method end");
        } else if (text.contains(SHOW_HISTORY_COMMAND)) {
            new MessageWriter(socket).write("history: {\n" + history.stream().collect(Collectors.joining("\t\n")) + " }\n####");
        } else {
            chatWorkers.broadcast(text);
        }
    }

    public void send(String text) {
        writer.write(text);
        history.add(text);
    }

    public void sendFile(File file, Socket socket) {
//        String filepath = fileName();
//        File file = new File(filepath);
        fileService = new FileService(file);
        fileService.sendFile(socket);
    }

    public void saveFile(String text) {
//        fileName();
        fileService = new FileService();
//        fileService.saveFile(text);
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

