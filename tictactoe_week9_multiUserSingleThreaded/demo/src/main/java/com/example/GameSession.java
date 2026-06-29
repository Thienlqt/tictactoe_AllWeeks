package com.example;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class GameSession {
    private SocketChannel client;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private StringBuilder msgBuilder = new StringBuilder();
    private Board board;
    
    private boolean isInitialized = false;
    private final int humanMark = 1;
    private final int compMark = 2;

    public GameSession(SocketChannel client) {
        this.client = client;
        this.board = new Board();
    }

    public void readAndProcess() throws IOException {
        buffer.clear();
        int bytesRead = client.read(buffer);
        if (bytesRead == -1) {
            throw new IOException("Client disconnected");
        }
        
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        msgBuilder.append(new String(bytes));

        // Read line by line in case multiple messages are bundled in one buffer
        String messages = msgBuilder.toString();
        int newlineIdx;
        while ((newlineIdx = messages.indexOf('\n')) != -1) {
            String line = messages.substring(0, newlineIdx).trim();
            messages = messages.substring(newlineIdx + 1);
            msgBuilder = new StringBuilder(messages);

            if (!line.isEmpty()) {
                handleInput(line);
            }
        }
    }

    private void handleInput(String input) throws IOException {
        if (input.equalsIgnoreCase("QUIT")) {
            client.close();
            return;
        }

        // The first message is always who starts (1 or 2)
        if (!isInitialized) {
            int starter = Integer.parseInt(input);
            isInitialized = true;

            send("Hello!\n\n");
            sendBoard();

            if (starter == compMark) {
                playComputerTurn();
            } else {
                promptHuman();
            }
        } else {
            // Processing a human's move
            int cell = Integer.parseInt(input); // Assumes no cheat/always valid 1-9
            if (board.isOccupied(cell)) {
                send("The cell is occupied!\n");
                promptHuman(); // Prompt the human to try again
                return;        // Stop processing this move
            }
            board.placeMark(cell, humanMark);
            sendBoard();

            if (checkWinOrDraw(humanMark)) return;
            playComputerTurn();
        }
    }

    private void playComputerTurn() throws IOException {
        send("Player#" + compMark + "'s turn\n");
        
        // Basic strategy: Find the first available cell
        for (int i = 1; i <= 9; i++) {
            if (!board.isOccupied(i)) {
                board.placeMark(i, compMark);
                break;
            }
        }
        sendBoard();

        if (checkWinOrDraw(compMark)) return;
        promptHuman();
    }

    private void promptHuman() throws IOException {
        send("Player#" + humanMark + "'s turn\n");
        send("TURN_PROMPT\n");
    }

    private boolean checkWinOrDraw(int mark) throws IOException {
        if (board.hasWon(mark)) {
            send("Player#" + mark + " won!\n");
            send("End of the game\n");
            client.close();
            return true;
        }
        if (board.isFull()) {
            send("It is a draw!\n");
            send("End of the game\n");
            client.close();
            return true;
        }
        return false;
    }

    private void send(String msg) throws IOException {
        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());
        while (outBuffer.hasRemaining()) {
            client.write(outBuffer);
        }
    }

    private void sendBoard() throws IOException {
        send(board.getDisplayString());
    }
}