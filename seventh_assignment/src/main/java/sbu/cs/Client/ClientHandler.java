package sbu.cs.Client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private DataInputStream in;
    private DataOutputStream out;
    private List<ClientHandler> clients = new ArrayList<>();

    public ClientHandler(Socket socket , List<ClientHandler> clients) throws IOException {
        this.socket = socket;
//        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        this.clients = clients;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public void run() {
//        try {
//            String clientName = reader.readLine();
//            String message;
//            while ((message = reader.readLine()) != null) {
//                ChatServer.broadcast(clientName + ": " + message, this);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try{
            String massage;
            while(true){
                massage =in.readUTF();
                sendAll(massage);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void sendAll(String massage) throws IOException {
        for(ClientHandler client : clients){
            DataOutputStream out = new DataOutputStream(client.getOut());
            out.writeUTF(massage);
        }
    }
}
