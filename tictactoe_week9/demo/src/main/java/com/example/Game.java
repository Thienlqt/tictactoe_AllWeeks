package com.example;

import java.io.BufferedReader;
import java.io.PrintWriter;

class Game {
    private Board board;
    private Player human;
    private Player computer;
    private Player currentPlayer;
    private PrintWriter out;

    public Game(BufferedReader in, PrintWriter out) {
        // The game sets up its own board and players when it is created
        this.out = out;
        this.board = new Board(out);
        this.human = new HumanPlayer("Human", 1, out, in);
        this.computer = new ComputerPlayer("Computer", 2);
    }

    // Helper method to keep the game loop clean
    private void switchPlayer() {
        if (currentPlayer == human) {
            currentPlayer = computer;
        } else {
            currentPlayer = human;
        }
    }

    // This method takes the starter argument and runs the game
    public void start(int starter) {
        try {
            if (starter != 1 && starter != 2) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            out.println("Invalid argument. Please use 1 or 2.");
            out.println("End of the game");
            return;
        }
        // 1. Set the starting player
        if (starter == 1) {
            currentPlayer = human;
        } else {
            currentPlayer = computer;
        }

        out.println("Hello!\n");

        // 2. Show the empty board
        board.display();

        // 3. Begin the game loop
        while (true) {
            out.println("Player#" + currentPlayer.getMark() + "'s turn");
            int chosenCell = currentPlayer.makeMove(board);
            board.placeMark(chosenCell, currentPlayer.getMark());

            board.display();

            if (board.hasWon(currentPlayer.getMark())) {
                out.println("Player#" + currentPlayer.getMark() + " won!");
                out.println("End of the game");
                break;
            }

            if (board.isFull()) {
                out.println("It is a draw!");
                out.println("End of the game");
                break;
            }

            // If no one won and it's not a draw, switch turns!
            switchPlayer();
        }
    }
}
