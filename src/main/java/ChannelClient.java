import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Slf4j
class ChannelClient {

    private SocketChannel channel;
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    void start() {
        channel = createSocketChannel();
    }

    private SocketChannel createSocketChannel() {
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(HOST, PORT));
            log.info("New channel connected");
        } catch (Exception e) {
            log.error("Uknown host " + HOST);
        }
        return channel;
    }
}
