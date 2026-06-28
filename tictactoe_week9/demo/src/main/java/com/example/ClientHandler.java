package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket connectionSocket = new Socket();

    public ClientHandler(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
    }

    @Override
    public void run() {
        while (true) {
            try (PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));) {

                String turnArg = in.readLine();
                if (turnArg == null)
                    return;

                int starter = Integer.parseInt(turnArg);

                Game game = new Game(in, out);
                game.start(starter);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    connectionSocket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }

}
