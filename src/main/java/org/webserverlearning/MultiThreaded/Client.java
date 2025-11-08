package org.webserverlearning.MultiThreaded;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    static void main() {
        Client client = new Client();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(client.getRunnable());
            thread.start();
        }
    }

    public Runnable getRunnable() {
        return () -> {
            int port = 8010;
            try {
                InetAddress address = InetAddress.getByName("localhost");
                Socket socket = new Socket(address, port);
                PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                toSocket.println("hello from the client");
                String line = fromSocket.readLine();
                System.out.println("response from the socket is " + line);
                toSocket.close();
                fromSocket.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
