import lombok.Getter;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Set;

@Getter
class FileService {

    private String fileName;
    private File file;
    private FileInputStream fileInputStream;
    private BufferedInputStream bufferedInputStream;
    private int fileSize;
    private byte[] filebyte;

    public FileService(File file) {
        this.file = file;
//        this.fileName = fileName;
        this.fileSize = (int) file.length();
        this.filebyte = new byte[fileSize];
    }

    public void saveFile() {
        try {
            String path = System.getProperty("user.home") + File.separator + "file_from_chat";
            file = new File(path);
            file.mkdir();
//            String path = file + File.separator + fileName;
            Files.writeString(Paths.get(path), "temp", StandardOpenOption.CREATE);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void sendFile(Socket socket) {
        try {
//            file = new File ("file.txt");
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            bufferedInputStream.read(filebyte,0,filebyte.length);
            OutputStream outputStream = socket.getOutputStream();
            System.out.println("Sending...");
            outputStream.write(filebyte,0,filebyte.length);
            outputStream.flush();
//            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }
}
