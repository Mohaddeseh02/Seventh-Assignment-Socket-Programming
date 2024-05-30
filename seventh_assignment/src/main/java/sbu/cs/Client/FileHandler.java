package sbu.cs.Client;

import java.io.*;
import java.net.Socket;

public class FileHandler implements Runnable {
    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;

    public FileHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new DataInputStream(socket.getInputStream());
        this.writer = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            sendFileList();

            String fileNum = reader.readUTF();
            int fileIndex = Integer.parseInt(fileNum) - 1;

            sendFile(fileIndex);
        } catch (IOException e) {
            System.err.println("Error handling file transfer: " + e.getMessage());
        }
    }

    private void sendFileList() throws IOException {
        File dir = new File("C:\\Users\\hp\\Desktop\\AP\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\sbu\\cs\\Server\\data");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null) {
            writer.writeUTF("No files available");
            writer.writeUTF("END");
            return;
        }

        for (int i = 0; i < files.length; i++) {
            writer.writeUTF((i + 1) + ". " + files[i].getName());
        }
    }

    private void sendFile(int fileIndex) throws IOException {
        File dir = new File("C:\\Users\\hp\\Desktop\\AP\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\sbu\\cs\\Server\\data");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files == null || fileIndex < 0 || fileIndex >= files.length) {
            writer.writeUTF("File not found");
            return;
        }

        File file = files[fileIndex];
        System.out.println("Sending file: " + file.getName() + " with size: " + file.length());

        writer.writeUTF(file.getName());
        writer.writeLong(file.length());
        writer.flush();

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[4 * 1024];
        int bytes;
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            writer.write(buffer, 0, bytes);
        }
        fileInputStream.close();
        System.out.println("File " + file.getName() + " sent to client.");
    }
}
