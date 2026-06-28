package com.example;

import java.io.PrintWriter;

public class Board 
{
    private int[][] grid;
    private PrintWriter out;
    // board
    public Board(PrintWriter out) {
        grid = new int[3][3];
        this.out = out;
    }

    public void display() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                out.print(grid[i][j]);
                if (j < 2) out.print(" | ");
            }
            out.println();
        }
        out.println();
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
        if (grid[0][1] == mark && grid[1][1] == mark && grid[2][1] == mark) return true;
        if (grid[0][2] == mark && grid[1][2] == mark && grid[2][2] == mark) return true;

        if (grid[0][0] == mark && grid[1][1] == mark && grid[2][2] == mark) return true;
        if (grid[0][2] == mark && grid[1][1] == mark && grid[2][0] == mark) return true;

        return false;
    }

    // CurrentPlayer.placeMark(Scanner scan, CurrentPlayer.getMark())

    public void placeMark(int cell, int mark) {
        int row = (cell - 1) / 3;
        int col = (cell - 1) % 3;
        if (!isOccupied(cell)) {
            grid[row][col] = mark;
        }
        else {
            out.println();
        }
    }
}
