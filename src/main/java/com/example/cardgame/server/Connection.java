package com.example.cardgame.server;

import com.example.cardgame.db.model.Account;

import java.io.*;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private Account account;

    private BufferedReader reader;
    private PrintWriter writer;

    private Thread inputListener;

    public Connection(Socket socket1) {
        socket = socket1;
        account = null;     ////

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
}
