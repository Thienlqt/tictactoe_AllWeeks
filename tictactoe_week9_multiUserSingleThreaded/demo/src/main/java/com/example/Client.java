package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        String currentBoardState = "0,0,0,0,0,0,0,0,0";
        String humanMark = "1";

        // Initial setup
        System.out.println("Hello!");
        displayBoard(currentBoardState);
        System.out.println("Player#1's turn");

        while (true) {
            String input = console.nextLine();

            // Check for quit condition
            if (input.equals("q")) {
                System.out.println("End of the game");
                break;
            }

            // Client-side validation for numbers and ranges
            try {
                int cell = Integer.parseInt(input.trim());
                if (cell < 1 || cell > 9) {
                    System.out.println("Please, input a valid number [1-9]");
                    System.out.println("Player#1's turn");
                    continue;
                }

                // Send request to stateless server
                String[] response = sendRequest(humanMark, currentBoardState, input);
                if (response == null) {
                    System.out.println("Network error. Please try again.");
                    continue;
                }

                String status = response[0];
                String boardState = response[1];
                String message = response.length > 1 ? response[2] : "NULL";

                switch (status) {
                    case "OCCUPIED":
                        System.out.println("The cell is occupied!");
                        System.out.println("Player#1's turn");
                        break;

                    case "WIN_HUMAN":
                        displayBoard(boardState);
                        System.out.println("Player#1 won!");
                        System.out.println("End of the game");
                        return;

                    case "DRAW":
                        displayBoard(boardState);
                        System.out.println("It is a draw!");
                        System.out.println("End of the game");
                        return;

                    case "WIN_COMP":
                        // Display human move board, then computer move board
                        displayBoard(boardState);

                        System.out.println("Player#2 won!");
                        System.out.println("End of the game");
                        return;

                    case "CONTINUE":
                        // Game continues: display human board, then computer board
                        displayBoard(boardState);
                        currentBoardState = boardState; // Update local state memory
                        System.out.println("Player#1's turn");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please, input a valid number [1-9]");
                System.out.println("Player#1's turn");
            }
        }
        console.close();
    }

    private static String[] sendRequest(String mark, String boardState, String cell) {
        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Format: BOARD_STATE;CELL
            String payload = mark + ";" + boardState + ";" + cell;
            out.println(payload);

            String response = in.readLine();
            if (response != null) {
                return response.split(";");
            }
        } catch (Exception e) {
            // Handled in main loop
        }
        return null;
    }

    private static void displayBoard(String state) {
        String[] cells = state.split(",");
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(cells[index++]);
                if (j < 2) System.out.print(" | ");
            }
            System.out.println();
        }
    }
}