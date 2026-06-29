package com.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        try {
            // Open a non-blocking ServerSocketChannel and a Selector
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(8080));
            serverSocket.configureBlocking(false);
            
            // Register the server socket to accept incoming connections
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            // Maintain the game state for each active connection
            Map<SocketChannel, GameSession> sessions = new HashMap<>();
            System.out.println("NIO Server started on port 8080. Waiting for connections...");

            while (true) {
                // select() blocks until at least one channel is ready for the registered events
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;

                    if (key.isAcceptable()) {
                        // Accept a new client connection
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        SocketChannel client = ssc.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        
                        // Attach a new game session to this client
                        sessions.put(client, new GameSession(client));
                    } else if (key.isReadable()) {
                        // Handle incoming data from an existing client
                        SocketChannel client = (SocketChannel) key.channel();
                        GameSession session = sessions.get(client);
                        try {
                            session.readAndProcess();
                        } catch (IOException e) {
                            // Client disconnected abruptly
                            key.cancel();
                            client.close();
                            sessions.remove(client);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}