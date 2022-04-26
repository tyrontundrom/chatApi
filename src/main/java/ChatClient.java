import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

@Slf4j
@Getter
class ChatClient {
    private final Consumer<String> onText;
    private final Runnable readFromSocket;
    private final Runnable readFromConsole;
    private static final int PORT = 8080;
    private static final String HOST = "localhost";
    private static String name;
    private static String chatName;
    private Map<String, String> privateChatMap = new HashMap<>();

    public ChatClient(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        onText = text -> new MessageWriter(socket).write(this.name + ": " + text);
        readFromSocket = () -> new MessageReader(socket, System.out::println, () -> {
        }).read();
        readFromConsole = () -> new MessageReader(System.in, onText).read();
    }

    public static void main(String[] args) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
        new ChatClient(HOST, PORT).start();
    }

    private void start() {
        menu();
        log.info(name + " connected to chat");
        new Thread(readFromSocket).start();
        Thread consoleMessageReader = new Thread(readFromConsole);
        consoleMessageReader.setDaemon(true);
        consoleMessageReader.start();
    }

    private void menu() {
        Scanner in = new Scanner(System.in);
        System.out.print("Wyjście z czatu \"\\q\"" +
                "\nNowy pokój \"\\r\"" +
                "\nPodaj swoją nazwę:");
        name = in.nextLine();

        MessageReader.showArchive().forEach(System.out::println);

    }
}
