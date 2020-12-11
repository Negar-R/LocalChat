# LocalChat

> Having an active connection opened between the client and the server so client can send and receive data. This allows real-time communication using TCP sockets. This is made possible by java.net.ServerSocket.
After running the Main.java you should specify that you are connecting as a client or a server. 
Notice: The server should be upped first.

## Run project locally

First, clone the repository to your local machine:
```
git clone https://github.com/Negar-R/LocalChat.git
cd LocalChat
```

as `Maven` is not used to build dependency tree for project, files should be compiled in the below order:

```
javac -d . Message.java
javac -d . MessageObserver.java
javac -d . MessageSubject.java
javac -d . BeepMessage.java
javac -d . TextMessage.java
javac -d . ConnectionManager.java
javac -d . Server.java
javac -d . Client.java
javac -d . Main.java
```

to run the project use the following command:

```
java localchat.Main
```

## Different types of message 
- text message
- beep message: you can write `beep` to play beep sound.
- end message: you can write `end` to close the connection.
