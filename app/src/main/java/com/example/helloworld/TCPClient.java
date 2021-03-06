package com.example.helloworld;
import java.net.*;
import java.io.*;

public class TCPClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException{
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMessage(String msg) throws IOException{
        out.println(msg);
       // String response = in.readLine();
       // return response;
    }

    public void stopConnection() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
    }

    public boolean isConnected() {
        return clientSocket.isConnected();
    }
}
