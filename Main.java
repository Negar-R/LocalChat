package localchat;

import java.util.Scanner;

public class Main {
    public static class SimpleObserver implements MessageObserver {
        @Override
        public void update(Message message) {
            message.execute();
        }
    }
    
    public static void main(String[] args){
        SimpleObserver simpleObserver = new SimpleObserver();

        System.out.println("Please enter connection type: (server or client)");

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (command.equals(ConnectionManager.SERVER)){
            System.out.println("Creating server...");
            ConnectionManager connectionManager = new ConnectionManager(
                ConnectionManager.SERVER, "localhost", simpleObserver
            );
        } else {
            System.out.println("Creating client...");
            ConnectionManager connectionManager = new ConnectionManager(
                ConnectionManager.CLIENT, "localhost", simpleObserver
            );
        }
    }
}
