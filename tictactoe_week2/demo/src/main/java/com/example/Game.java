package com.example;

class Game {
    private Board board;
    private Player human;
    private Player computer;
    private Player currentPlayer;

    public Game() {
        // The game sets up its own board and players when it is created
        board = new Board();
        human = new HumanPlayer("Human", 1);
        computer = new ComputerPlayer("Computer", 2);
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
            System.out.println("Invalid argument. Please use 1 or 2.");
            return;
        }
        // 1. Set the starting player
        if (starter == 1) {
            currentPlayer = human;
        } else {
            currentPlayer = computer;
        }

        // 2. Show the empty board
        board.display();

        // 3. Begin the game loop
        while (true) {
            int chosenCell = currentPlayer.makeMove(board);
            board.placeMark(chosenCell, currentPlayer.getMark());

            board.display();

            if (board.hasWon(currentPlayer.getMark())) {
                System.out.println("Player " + currentPlayer.getName() + " won!");
                break;
            }

            if (board.isFull()) {
                System.out.println("It is a draw!");
                break;
            }

            // If no one won and it's not a draw, switch turns!
            switchPlayer();
        }
    }
}
