package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);

            while (true) {
                try (Socket connectionSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));) {

                    String turnArg = in.readLine();

                    int starter = Integer.parseInt(turnArg);

                    Game game = new Game(in, out);
                    game.start(starter);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}