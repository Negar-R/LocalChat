package localchat;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class Server {  
    private Socket socket = null;
    private DataInputStream console = null;
    private ServerSocket server = null;
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;

    private class Writer implements Runnable {
        private Thread t;
        private String threadName;
        private DataInputStream console;
        private DataOutputStream outputStream;
        
        public Writer(String name, DataOutputStream outputStream, DataInputStream console) {
            this.threadName = name;
            this.outputStream = outputStream;
            this.console = console;
        }
        @Override
        public void run() {
            String line = "";

            System.out.println("You may chat now ([String], end, beep):");

            while (!line.equals("end")) { 
                try {
                    line = this.console.readLine();
                    this.outputStream.writeUTF(line);
                    this.outputStream.flush();
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        
        public void start () {
            if (this.t == null) {
                this.t = new Thread(this, threadName);
                this.t.start ();
            }
        }
    }

    public Server(int port, MessageSubject messageSubject) { 
        try {
            server = new ServerSocket(port);  
            System.out.println("Server started: " + server);
            System.out.println("Waiting for a client ..."); 
            socket = server.accept();
            System.out.println("Client accepted: " + socket);

            start(socket);

            open(); // !
            boolean done = false;
            while (!done) { 
                try {
                    String line = inputStream.readUTF(); // input stream
                    if (line.equals("beep")) {
                        messageSubject.notifyAllObservers(new BeepMessage());
                    } else {
                        messageSubject.notifyAllObservers(new TextMessage(line));
                    }
                    done = line.equals("end");
                }
                catch(IOException ioe) {
                    done = true;
                }
            }
            close();
        }
        catch(IOException ioe) {
            System.out.println(ioe); 
        }
    }
    
    public void open() throws IOException {
        this.inputStream = new DataInputStream(
            new BufferedInputStream(socket.getInputStream())
        );
    }
    
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public void start(Socket socket) throws IOException {
        this.console = new DataInputStream(System.in);
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        Writer writer = new Writer("WriterThread-1", this.outputStream, this.console);
        writer.start();
    }
}