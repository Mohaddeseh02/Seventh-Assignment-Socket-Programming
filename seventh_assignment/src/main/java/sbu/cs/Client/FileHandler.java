package sbu.cs.Client;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class FileHandler implements Runnable {
    private Socket socket;
    DataOutputStream outputStream;
    DataInputStream inputStream;
    public FileHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
    }
    @Override
    public void run() {
        File dir = new File("C:\\Users\\Lenovo\\Desktop\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\sbu\\cs\\Server\\data");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        try {
            String filesName ="";
            int i = 1;
            for (File file : files) {
                filesName = filesName + i +". " + file.getName() + "\n";
                i++;
            }
            outputStream.writeUTF(filesName);
            String option = inputStream.readUTF();
            System.out.println(option);
            int op = Integer.parseInt(option);
            File selectedFile = files[op - 1];
            outputStream.writeUTF(selectedFile.getName());
            BufferedReader fileReader = new BufferedReader(new FileReader(selectedFile));
            String line ="";
            String temp;
            while ((temp = fileReader.readLine()) != null) {
                line = line + temp;
            }

            outputStream.writeUTF(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendFileList(DataOutputStream out) throws IOException {

        // Get a list of all files
        File directory = new File("C:\\Users\\hp\\Desktop\\AP\\Seventh-Assignment-Socket-Programming\\seventh_assignment\\src\\main\\java\\sbu\\cs\\Server\\data");
        File[] files = directory.listFiles();

        // Write number of files
        out.writeInt(files.length);

        // Write each file name
        for(File file : files) {

            String fileName = file.getName();

            out.writeUTF(fileName);

        }

        // End signal
        out.writeUTF("END");

    }
}
