package com.example;

import java.io.BufferedReader;
import java.io.PrintWriter;

class HumanPlayer extends Player {
    private PrintWriter out;
    private BufferedReader in;

    public HumanPlayer(String name, int mark, PrintWriter out, BufferedReader in) {
        super(name, mark);
        this.in = in;
        this.out = out;
    }

    @Override
    public int makeMove(Board board){
        while (true) {
            out.println("TURN_PROMPT");
            try {
                String input = in.readLine();
                if (input == null) return -1;

                if (input.equalsIgnoreCase("QUIT")) {
                    return -1;
                }

                int cell = Integer.parseInt(input);
                if (cell >= 1 && cell <= 9 && !Double.isNaN(cell)) {
                    if (board.isOccupied(cell)) {
                        out.println("The cell is occupied!");
                        out.println("Player#" + this.getMark() + "'s turn"); // print human turne
                    } else {
                        return cell; // Valid move found
                    }
                } else {
                    out.println("Please, input a valid number [1-9] or 'q' to quit");
                    out.println("Player#" + this.getMark() + "'s turn");
                }
            } catch (NumberFormatException e) {
                // Catches strings that cannot be parsed into an integer
                out.println("Please, input a valid number [1-9] or 'q' to quit");
                out.println("Player#" + this.getMark() + "'s turn");
            }
            catch (Exception e) {
                out.println("Network error reading input.");
                return -1;
            }
        }
    }
}
