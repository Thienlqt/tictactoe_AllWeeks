package com.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(8080));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Stateless NIO Server started on port 8080. Waiting for connections...");

            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;

                    if (key.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        SocketChannel client = ssc.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        handleRequest(key);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleRequest(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int bytesRead = client.read(buffer);
            if (bytesRead == -1) {
                client.close();
                key.cancel();
                return;
            }

            buffer.flip();
            String payload = new String(buffer.array(), 0, buffer.limit()).trim();
            
            // Expected Format: "MARK;BOARD_STATE;CELL" (e.g., "1;0,0,0,0,0,0,0,0,0;5")
            String[] parts = payload.split(";");
            if (parts.length != 3) {
                client.close();
                return;
            }

            int humanMark = Integer.parseInt(parts[0]);
            String boardState = parts[1];
            String moveStr = parts[2];
            int compMark = (humanMark == 1) ? 2 : 1;

            Board board = new Board(boardState);

            // 1. Process human move (if it's not the initialization step)
            if (!moveStr.equals("START")) {
                int cell = Integer.parseInt(moveStr);
                if (board.isOccupied(cell)) {
                    sendResponse(client, "OCCUPIED", board.serialize(), "The cell is occupied!\nPlayer#" + humanMark + "'s turn");
                    return;
                }
                board.placeMark(cell, humanMark);
            }

            // Check if human won or draw
            if (board.hasWon(humanMark)) {
                sendResponse(client, "WIN_HUMAN", board.serialize(), "Player#" + humanMark + " won!");
                return;
            } else if (board.isFull()) {
                sendResponse(client, "DRAW", board.serialize(), "It is a draw!");
                return;
            }

            // 2. Process Computer Turn
            boolean compMoved = false;
            for (int i = 1; i <= 9; i++) {
                if (!board.isOccupied(i)) {
                    board.placeMark(i, compMark);
                    compMoved = true;
                    break;
                }
            }

            if (compMoved) {
                if (board.hasWon(compMark)) {
                    sendResponse(client, "WIN_COMP", board.serialize(), "Player#" + compMark + " won!");
                    return;
                } else if (board.isFull()) {
                    sendResponse(client, "DRAW", board.serialize(), "It is a draw!");
                    return;
                }
            }

            // 3. Return success and ask for next move
            sendResponse(client, "CONTINUE", board.serialize(), "Player#" + humanMark + "'s turn");

        } catch (Exception e) {
            try {
                client.close();
                key.cancel();
            } catch (IOException ex) {
                // Ignore
            }
        }
    }

    private static void sendResponse(SocketChannel client, String status, String boardState, String message) throws IOException {
        // Send: STATUS;BOARD;MESSAGE
        String response = status + ";" + boardState + ";" + message + "\n";
        ByteBuffer outBuffer = ByteBuffer.wrap(response.getBytes());
        while (outBuffer.hasRemaining()) {
            client.write(outBuffer);
        }
        // STATELESS MAGIC: Close the connection instantly
        client.close(); 
    }
}