package org.webserverlearning.ThreadPool;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadPool;

    public Server(int poolSize) {
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    static void main() {
        int port = 8010;
        int poolSize = 100;
        Server server = new Server(poolSize);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(70000);
            IO.println("Server is listening on port" + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                server.threadPool.execute(() -> {
                    try {
                        server.handleClient(clientSocket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            {
                server.threadPool.shutdown();
            }
        }
    }

    public void handleClient(Socket clientSocket) throws IOException {
        try (PrintWriter toSocket = new PrintWriter(clientSocket.getOutputStream(), true)) {
            toSocket.println("hello from server" + clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
