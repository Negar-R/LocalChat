package localchat;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Server {  
    private Socket socket = null;
    private DataInputStream console = null;
    private ServerSocket server = null;
    private DataInputStream inputStream = null;
    private ArrayList<DataOutputStream> outputStreams = null;
    private int clientCount = 0;

    private class Writer implements Runnable {
        private Thread t;
        private String threadName;
        private DataInputStream inputStream;
        private ArrayList<DataOutputStream> outputStreams;
        
        public Writer(String name, ArrayList<DataOutputStream> outputStreams,
                      DataInputStream inputStream) {
            this.threadName = name;
            this.outputStreams = outputStreams;
            this.inputStream = inputStream;
        }
        
        public void run() {
            String line = "";

            System.out.println("You may chat now ([String], end, beep):");

            while (!line.equals("end")) { 
                try {
                    line = inputStream.readUTF();
                    for (DataOutputStream outputStream : this.outputStreams) {
                        outputStream.writeUTF(line);
                        outputStream.flush();
                    }
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        
        public void start () {
            if (this.t == null) {
                this.t = new Thread(this, threadName);
                this.t.start();
            }
        }
    }

    public Server(int port, MessageSubject messageSubject) { 
        try {
            this.outputStreams = new ArrayList<>();
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);

            while (true){
                System.out.println("Waiting for a client ..."); 
                socket = server.accept();
                System.out.println("Client accepted: " + socket);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                start(socket);
            }
        }
        catch(IOException ioe) {
            System.out.println(ioe); 
        }
    }

    public void start(Socket socket) throws IOException {
        inputStream = new DataInputStream(
            new BufferedInputStream(socket.getInputStream())
        );
        this.outputStreams.add(new DataOutputStream(socket.getOutputStream()));
        Writer writer = new Writer("WriterThread:" + this.clientCount++, this.outputStreams, inputStream);
        writer.start();
    }
}