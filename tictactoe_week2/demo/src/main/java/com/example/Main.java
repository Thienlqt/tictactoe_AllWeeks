package com.example;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide an argument: 1 (Human starts) or 2 (Computer starts).");
            return;
        }

        int starter = Integer.parseInt(args[0]);

        // Create the game instance and start it, passing the argument
        Game game = new Game();
        game.start(starter);
    }
}
