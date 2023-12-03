package com.example.cardgame.client;

import com.example.cardgame.properties.ServerProperties;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class Client {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter out;
    private Thread inputListener;
    private String name = UUID.randomUUID().toString().substring(1, 5);     ///////////

    public Client() {
        try {
            clientSocket = new Socket(ServerProperties.getHost(), ServerProperties.getPort());

            InputStream inputStream = clientSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            OutputStream outputStream = clientSocket.getOutputStream();
            out = new PrintWriter(outputStream, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getOut() {
        return out;
    }

    public Thread getInputListener() {
        return inputListener;
    }

    public void setInputListener(Thread inputListener) {
        this.inputListener = inputListener;
        inputListener.start();
    }

    public void closeSocket() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(String text) {
        out.println(text);
    }

    public String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
