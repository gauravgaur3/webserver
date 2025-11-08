package org.webserverlearning.MultiThreaded;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    static void main() throws IOException {
        int port = 8010;
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
        IO.println("Server Listening to port : " + port);
        Server server = new Server();
        while (true) {
            Socket acceptedSocket = serverSocket.accept();
            Thread thread = new Thread(() -> server.getConsumer().accept(acceptedSocket));
//          Alternative Way
//          Thread thread = new Thread(new ClientHandler(acceptedSocket));
            thread.start();
        }
    }

    public Consumer<Socket> getConsumer() {
        return (clientSocket) -> {
            try {
                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());
                toClient.println("hello from server");
                toClient.close();
                clientSocket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

//    class ClientHandler implements Runnable {
//        private final Socket clientSocket;
//
//        public ClientHandler(Socket clientSocket) {
//            this.clientSocket = clientSocket;
//        }
//
//        @Override
//        public void run() {
//            try {
//                PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream());
//                toClient.println("hello from server");
//                toClient.flush();
//                toClient.close();
//                clientSocket.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
