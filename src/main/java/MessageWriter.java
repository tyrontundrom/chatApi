import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
@ToString
class MessageWriter {
    private PrintWriter writer;

    public MessageWriter(Socket socket) {
        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            log.error("Creating output stream failed: " + e.getMessage());
        }
    }

    public void write(String text) {
        writer.println(text);
    }


}
