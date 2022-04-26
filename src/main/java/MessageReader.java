import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Getter
class MessageReader {
    private final Consumer<String> onText;
    private BufferedReader reader;
    private Runnable onClose;
    private List<String> archiveChat = new ArrayList<>();
    private static ArchiveChat archive = new ArchiveChat();

    public MessageReader(InputStream inputStream, Consumer<String> onText) {
        this.onText = onText;
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public MessageReader(Socket socket, Consumer<String> onText, Runnable onClose) {
        this.onText = onText;
        this.onClose = onClose;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            log.info(Thread.currentThread().getName());
        } catch (IOException e) {
            log.error("Creating input stream failed: " + e.getMessage());
        }
    }

    public void read() {
        String text;
        try {
            while ((text = reader.readLine()) != null) {
                onText.accept(text);
                archiveChat.add(text);
                this.archive.add(text);
            }
        } catch (IOException e) {
            log.warn("Read message failed: " + e.getMessage());
        }
        finally {
            if (onClose != null) {
                onClose.run();
            }
            archiveChat.forEach(System.out::println);
        }
    }

    static List<String> showArchive() {
       return archive.getArchive();
    }

}
