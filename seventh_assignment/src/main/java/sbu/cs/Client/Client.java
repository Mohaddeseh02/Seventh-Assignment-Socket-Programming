package sbu.cs.Client;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;
    private static DataInputStream reader;
    private static DataOutputStream writer;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());

        System.out.println("Enter your name: ");
        String name = new BufferedReader(new InputStreamReader(System.in)).readLine();

        writer.flush();

        new Thread(() -> {
            while (true) {

            }
        }).start();

        while (true) {
            System.out.println("1. Send a message");
            System.out.println("2. Download a file");
            System.out.println("3. Disconnect");
            System.out.print("Enter your choice: ");
            String choice = new BufferedReader(new InputStreamReader(System.in)).readLine();

            switch (choice) {
                case "1":
                    writer.writeUTF("chat");
                    reader.readUTF();
                    writer.writeUTF(name + " connected.");
                    System.out.println (reader.readUTF ());
                    while (true) {
                        String message = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        if (message == null || message.equals(name + ": " + null) || message.equals ("/disconnect")) {
                            writer.writeUTF(name + " disconnected from chat");
                            System.out.println (reader.readUTF ());
                            break;
                        }
                        writer.writeUTF(name + ": " + message);
                        System.out.println (reader.readUTF ());
                    }
                    break;
                case "2":
                    writer.writeUTF("file");
                    reader.readUTF ();
                    for (int i = 0; i <= 9; i++)
                    {
                        System.out.println (reader.readUTF());
                    }

                    System.out.println("Enter file number to download:");
                    Scanner scanner = new Scanner(System.in);
                    String fileNum = scanner.nextLine();
                    writer.writeUTF(fileNum);

                    receiveFile();
                    break;
                case "3":
                    writer.writeUTF(name + " disconnected");
                    reader.readUTF ();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void receiveFile() {
        try {
            String fileName = reader.readUTF ();
            System.out.println("Received file name: " + fileName);
            long size = reader.readLong();
            System.out.println("Received file size: " + size);

            File dir = new File("C:\\Users\\hp\\Desktop\\AP\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\sbu\\cs\\Server\\data" + fileName);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(new File(dir, fileName));

            byte[] buffer = new byte[4 * 1024];
            int bytes;
            while (size > 0 && (bytes = reader.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes;
            }
            fileOutputStream.close();
            System.out.println("File " + fileName + " downloaded successfully.");
//            while (true){
//                if(fileName.equals("/menu")){
//                    break;
//                }
//            }
        } catch (IOException e) {
            System.err.println("Error receiving file: " + e.getMessage());
        }
//        Scanner scanner = new Scanner (System.in);
//        scanner.nextLine ();
    }

}
