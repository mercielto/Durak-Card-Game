package com.example.cardgame.server;

import com.example.cardgame.db.model.Account;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Connection {
    private final Socket socket;
//    private final Account account;
    private final String name;

    private final BufferedReader reader;
    private final PrintWriter writer;

    private Thread inputListener;

    public Connection(Socket socket1) {
        socket = socket1;
//        account = null;     ////
        name = UUID.randomUUID().toString().substring(1, 5);

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();
            writer = new PrintWriter(outputStream, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setListener(Thread listener) {
        inputListener = listener;
        inputListener.start();
    }

    public void write(String text) {
        writer.println(text);
    }

    public BufferedReader getReader() {
        return reader;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Connection) {
            if (socket.equals(((Connection) obj).socket)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }
}
