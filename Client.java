package localchat;

import java.net.*;
import java.io.*;

public class Client {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream console = null;
    private DataOutputStream outputStream = null;
    private DataInputStream inputStream = null;

    private class Reader implements Runnable {
        private Thread t;
        private String threadName;
        private Socket socket;
        private DataInputStream inputStream;
        private MessageSubject messageSubject;
        
        public Reader(String name, Socket socket, MessageSubject messageSubject) {
            this.threadName = name;
            this.socket = socket;
            this.messageSubject = messageSubject;
            System.out.println("Creating: " + threadName);
        }
        @Override
        public void run() {
            try {
                open();
                boolean done = false;
                while (!done) { 
                    String line = this.inputStream.readUTF();
                    if (line.equals("beep")) {
                        messageSubject.notifyAllObservers(new BeepMessage());
                    } else {
                        messageSubject.notifyAllObservers(new TextMessage(line));
                    }
                    done = line.equals("end");      
                }
                close();
            } catch(IOException ioe) {
                System.out.println("I'm here !!!! ");
                ioe.printStackTrace();
            }
        }
        
        public void start () {
            System.out.println("Starting " +  threadName );
            if (this.t == null) {
                this.t = new Thread (this, threadName);
                this.t.start ();
            }
        }

        public void open() throws IOException {
            this.inputStream = new DataInputStream(
                new BufferedInputStream(this.socket.getInputStream())
            );
        }
        
        public void close() throws IOException {
            if (this.socket != null) {
                this.socket.close();
            }
            if (this.inputStream != null) {
                this.inputStream.close();
            }
            System.exit(0);
        }
    }

    public Client(String serverName, int serverPort, MessageSubject messageSubject) {
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        } catch(UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch(IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }

        Reader reader = new Reader("ReaderThread-1", socket, messageSubject);
        reader.start();
        
        System.out.println("You may chat now ([String], end, beep):");

        String line = "";
        while (!line.equals("end")) { 
            try {
                line = console.readLine();
                outputStream.writeUTF(line);
                outputStream.flush();
            } catch(IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
            }
        }
    }

    public void start() throws IOException {
        console = new DataInputStream(System.in);
        outputStream = new DataOutputStream(socket.getOutputStream());
    }
    
    public void stop() {
        try {
            if (console != null) {
                console.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch(IOException ioe) {
            System.out.println("Error closing ...");
        }
    }
}