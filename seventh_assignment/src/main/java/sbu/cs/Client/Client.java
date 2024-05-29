package sbu.cs.Client;

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1234;
    private static DataInputStream reader;
    private static DataOutputStream writer;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        reader =new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());

        System.out.println("Enter your name: ");
        String name = new BufferedReader(new InputStreamReader(System.in)).readLine();

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
                    writer.writeUTF("chat");
                    writer.writeUTF(name + " connected.");
                    while (true) {
                        String message = new BufferedReader(new InputStreamReader(System.in)).readLine();
                        if(Objects.equals(message, name + ": " + null)){
                            writer.writeUTF(name + " disconnected from chat");
                            break;
                        }
                        writer.writeUTF(name + ": " + message);
//                        writer.flush();
                    }
                    break;
                case "2":
//                    writer.writeUTF("file");
//                    System.out.print(reader.readUTF());
////                    System.out.print("Enter the name of the file to download: ");
//                    String file = new BufferedReader(new InputStreamReader(System.in)).readLine();
//                    writer.writeUTF(file);
                    writer.writeUTF("file");

//                    receiveFileList(reader);

                    System.out.println("Enter file number to download");
                    Scanner scanner = new Scanner(System.in);
                    String fileNum = scanner.nextLine();
                    writer.writeUTF(fileNum);
                    receiveFile();
                    System.out.println("salam");

//                    String fileName = reader.readUTF();
//                    String text = reader.readUTF();
//                    File selectedFile = new File("C:\\Users\\hp\\Desktop\\AP\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\sbu\\cs\\Client\\"+ fileName);
//                    FileWriter fileWriter = new FileWriter(selectedFile);
//                    fileWriter.write(text);
//                    fileWriter.close();
                    break;
                case "3":
                    writer.writeUTF(name + " disconnected");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
//    static void receiveFile(String fileName, DataInputStream reader) throws IOException {
//        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName));
//        String line;
//        while (!(line = reader.readLine()).equals("END")) {
//            fileWriter.write(line);
//            fileWriter.newLine();
//        }
//        fileWriter.close();
//    }
//    static void receiveFileList(DataInputStream reader) throws IOException {
//        String line;
//        while(!(line = reader.readLine()).equals("END")) {
//            System.out.println(line);
//        }
//    }
public static void receiveFile () {
    int bytes = 0;
    try {
        String name = reader.readUTF();
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\hp\\Desktop\\AP\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\sbu\\cs\\Server\\data"+name);
        long size = reader.readLong();
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = reader.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;

        }
        fileOutputStream.close();
    }catch(IOException e){
        System.err.println(e.getMessage());
        System.out.println("error");
    }
}
}
