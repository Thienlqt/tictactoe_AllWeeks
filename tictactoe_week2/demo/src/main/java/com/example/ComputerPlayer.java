package com.example;

class ComputerPlayer extends Player {
    
    public ComputerPlayer(String name, int mark) {
        super(name, mark);
    }

    @Override
    public int makeMove(Board board) {
        // Basic strategy: Find the first available cell from 1 to 9
        for (int i = 1; i <= 9; i++) {
            if (!board.isOccupied(i)) {
                return i;
            }
        }
        return -1; // Fallback, shouldn't be reached if board isn't full
    }
}