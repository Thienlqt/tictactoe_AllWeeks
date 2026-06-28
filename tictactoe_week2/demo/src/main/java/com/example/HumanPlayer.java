package com.example;

import java.util.*;

class HumanPlayer extends Player {
    private Scanner scanner;

    public HumanPlayer(String name, int mark) {
        super(name, mark);
        scanner = new Scanner(System.in);
    }

    @Override
    public int makeMove(Board board) {
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                int cell = Integer.parseInt(input);
                if (cell >= 1 && cell <= 9) {
                    if (board.isOccupied(cell)) {
                        System.out.println("The cell is occupied!");
                    } else {
                        return cell; // Valid move found
                    }
                } else {
                    System.out.println("Please, input a valid number [1-9]");
                }
            } catch (NumberFormatException e) {
                // Catches strings that cannot be parsed into an integer
                System.out.println("Please, input a valid number [1-9]");
            }
        }
    }
}
