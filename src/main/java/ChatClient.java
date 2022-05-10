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
    private User user;
    private static Socket socket;
    private UsersListInterface usersListInterface = new UsersList();

    public ChatClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        onText = text -> new MessageWriter(socket).write(this.name + ": " + text);
        readFromSocket = () -> new MessageReader(socket, System.out::println, () -> {
        }).read();
        readFromConsole = () -> new MessageReader(System.in, onText).read();
        System.out.println(socket);
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
                "\nPrywatna wiadomość \"@\"nazwa użytkownika wiadomość" +
                "\nWysyłanie pliku \"#send\" nazwa pliku" +
                "\nOdbieranie pliku \"#save\"" +
                "\nHistoria \"#show\"" +
                "\nPodaj swoją nazwę:");
        name = in.nextLine();
        user = new User(name,socket);
        usersListInterface.addUser(name,socket);
//        user.addUser();
        System.out.println("userlist " + usersListInterface.getSocket(name));
        System.out.println("clientUser: " + user.getSocket("ww"));
        MessageReader.showArchive().forEach(System.out::println);
    }

    public static String getUsername() {
        return name;
    }

    public static Socket getUserSocket() {
        return socket;
    }

//    public static UsersListInterface getList() {
//        return usersListInterface;
//    }



}
