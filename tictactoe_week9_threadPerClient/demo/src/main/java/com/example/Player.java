package com.example;

abstract class Player {
    protected String name;
    protected int mark; // 1 for Human, 2 for Computer

    public Player(String name, int mark) {
        this.name = name;
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }

    // Abstract method: Every specific player must define HOW they make a move
    public abstract int makeMove(Board board);
}