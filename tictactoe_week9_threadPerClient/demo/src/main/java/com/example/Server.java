package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(8080);) {
            while (true) {
                Socket connectionSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(connectionSocket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
