package com.example;

public class Board {
    private int[][] grid;
     public Board() {
        grid = new int[3][3];
    }
    // Formats the grid natively to a string rather than pushing it to an output stream
    public String getDisplayString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(grid[i][j]);
                if (j < 2) sb.append(" | ");
            }
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == 0) return false;
            }
        }  
        return true;
    }

    public boolean isOccupied(int cell) {
        int row = (cell - 1) / 3;
        int col = (cell - 1) % 3;
        return grid[row][col] != 0;
    }

    public boolean hasWon(int mark) {
        if (grid[0][0] == mark && grid[0][1] == mark && grid[0][2] == mark) return true;
        if (grid[1][0] == mark && grid[1][1] == mark && grid[1][2] == mark) return true;
        if (grid[2][0] == mark && grid[2][1] == mark && grid[2][2] == mark) return true;
        if (grid[0][0] == mark && grid[1][0] == mark && grid[2][0] == mark) return true;
        if (grid[0][2] == mark && grid[1][2] == mark && grid[2][2] == mark) return true;
        if (grid[0][0] == mark && grid[1][1] == mark && grid[2][2] == mark) return true;
        if (grid[0][2] == mark && grid[1][1] == mark && grid[2][0] == mark) return true;
        return false;
            
    }

    public void placeMark(int cell, int mark) {
        int row = (cell - 1) / 3;
        int col = (cell - 1) % 3;
        grid[row][col] = mark;
    }
} 