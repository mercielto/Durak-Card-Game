package com.example.cardgame.server;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Connection {
    private final Socket socket;
    private String name = "Not set";

    private final BufferedReader reader;
    private final PrintWriter writer;

    private Thread inputListener;

    public Connection(Socket socket1) {
        socket = socket1;
//        account = null;     ////

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();
            writer = new PrintWriter(outputStream, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setListener(Thread listener) {
        listener.start();

        if (inputListener != null) {
            inputListener.interrupt();
        }
        inputListener = listener;
    }

    public void write(String text) {
        System.out.println("%s: send %s".formatted(name, text));
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

    public void setName(String name) {
        this.name = name;
    }
}
