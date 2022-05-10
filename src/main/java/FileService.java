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
    private DataInputStream dataInputStream;
    private int fileSize;
    private byte[] filebyte;


    public FileService() {
    }

    public FileService(File file) {
        this.file = file;
//        this.fileName = fileName;
        this.fileSize = (int) file.length();
        this.filebyte = new byte[fileSize];
    }

    private void saveFile(String path) {
        try {
            String savePath = System.getProperty("user.home") + File.separator + path;
            File file = new File(path);
            file.mkdir();
//            String path = file + File.separator + fileName;
            Files.writeString(Paths.get(savePath), "temp", StandardOpenOption.CREATE);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void receiveFile(String fileName, Socket socket) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        dataInputStream = new DataInputStream(socket.getInputStream());
        long size = dataInputStream.readLong();
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;
        }

        fileOutputStream.close();
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
