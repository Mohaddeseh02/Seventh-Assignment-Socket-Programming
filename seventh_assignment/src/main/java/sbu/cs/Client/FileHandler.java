package sbu.cs.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class FileHandler implements Runnable {
    private Socket socket;
    DataOutputStream outputStream;
    DataInputStream inputStream;
    public FileHandler(Socket socket){
        this.socket = socket;
//        this.inputStream = new DataInputStream();
    }
    @Override
    public void run() {
//        try{
//            String option =
//        }catch (IOException e){
//
//        }
    }
}
