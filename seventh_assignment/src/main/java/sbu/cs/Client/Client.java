package sbu.cs.Client;

import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;


    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

        System.out.println("Enter your name: ");
        String name = new BufferedReader(new InputStreamReader(System.in)).readLine();
        writer.writeUTF(name + " connected.");
//        writer.writeUTF("1");

        writer.flush();

        new Thread(() -> {
            try {
                while (true) {
                    System.out.println(new DataInputStream(socket.getInputStream()).readUTF());
                }
            } catch (IOException e) {
                e.printStackTrace();
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
                    System.out.print("Enter your message: ");
                    String message = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    writer.writeUTF(name + ": " + message);
                    break;
                case "2":
                    writer.writeUTF("LIST");
                    receiveFileList(reader);
                    System.out.print("Enter the name of the file to download: ");
                    String fileName = new BufferedReader(new InputStreamReader(System.in)).readLine();
                    writer.writeUTF("GET " + fileName);
                    receiveFile(fileName, reader);
                    break;
                case "3":
                    writer.writeUTF(name + " disconnected");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    static void receiveFileList(BufferedReader reader) throws IOException {
        String fileName;
        while (!(fileName = reader.readLine()).equals("END")) {
            System.out.println(fileName);
        }
    }

    static void receiveFile(String fileName, BufferedReader reader) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName));
        String line;
        while (!(line = reader.readLine()).equals("END")) {
            fileWriter.write(line);
            fileWriter.newLine();
        }
        fileWriter.close();
    }
}
