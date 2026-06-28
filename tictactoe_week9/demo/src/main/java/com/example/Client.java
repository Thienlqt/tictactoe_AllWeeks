package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please provide an argument: 1 (Human starts) or 2 (Computer starts).");
            return;
        }
        try (Socket clientSocket = new Socket("localhost", 8080);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                Scanner console = new Scanner(System.in);) {
            out.println(args[0]);
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                if (serverMessage.equals("TURN_PROMPT")) {
                    String move = console.nextLine();

                    if (move.equals("q")) {
                        System.out.println("End of the game");
                        out.println("QUIT");
                        clientSocket.close();
                        break;
                    }
                    out.println(move);
                } 
                else if (serverMessage.equals("End of the game")) {
                    System.out.println(serverMessage);
                    break;
                }
                else {
                    System.out.println(serverMessage);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
