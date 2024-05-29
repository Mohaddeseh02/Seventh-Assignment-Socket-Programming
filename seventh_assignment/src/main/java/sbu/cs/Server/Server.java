package sbu.cs.Server;

import sbu.cs.Client.ClientHandler;
import sbu.cs.Client.FileHandler;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Server Class
public class Server {
    private static final int PORT = 1234;
    private static List<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(8);
    private static ExecutorService pool2 = Executors.newFixedThreadPool(8);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server is running...");
        while (true) {
            Socket socket = serverSocket.accept();
            String option = new DataInputStream(socket.getInputStream()).readUTF();
            if(option.equals("chat")){
                System.out.println("[SERVER] client connected: " + socket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(socket , clients);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            }else if(option.equals("file")){
                System.out.println("[SERVER] client connected: " + socket.getInetAddress());
                FileHandler fileHandler = new FileHandler(socket);
                pool2.execute(fileHandler);
            }
        }
    }

    static void sendFileList(DataOutputStream writer) throws IOException {
        File dir = new File("C:\\Users\\hp\\Desktop\\AP\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\sbu\\cs\\Server\\data"); // directory to share files from
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        for (File file : files) {
            writer.writeUTF(file.getName());
        }
        writer.writeUTF("END"); // signal end of file list
    }

    static void sendFile(String fileName, DataOutputStream writer) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            writer.writeUTF("File not found");
            return;
        }
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = fileReader.readLine()) != null) {
            writer.writeUTF(line);
        }
        writer.writeUTF("END"); // signal end of file
    }

//    static void broadcast(String message, ClientHandler sender) throws IOException {
//        for (ClientHandler client : clients) {
//            if (client != sender) {
//                client.getWriter().write(message);
//            }
//        }
//    }
}


