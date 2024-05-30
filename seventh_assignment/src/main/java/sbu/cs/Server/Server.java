package sbu.cs.Server;

import sbu.cs.Client.ClientHandler;
import sbu.cs.Client.FileHandler;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());

            String option = reader.readUTF();
            writer.writeUTF(option);

            if (option.equals("chat")) {
                System.out.println("[SERVER] client connected for chat: " + socket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(socket, clients);
                clients.add(clientHandler);
                pool.execute(clientHandler);
            } else if (option.equals("file")) {
                System.out.println("[SERVER] client connected for file transfer: " + socket.getInetAddress());
                FileHandler fileHandler = new FileHandler(socket);
                pool2.execute(fileHandler);
            }
        }
    }
}

