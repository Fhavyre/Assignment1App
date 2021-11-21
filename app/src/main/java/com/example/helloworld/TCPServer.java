package com.example.helloworld;

import androidx.fragment.app.Fragment;

import com.example.helloworld.databinding.FragmentFirstBinding;

import java.net.*;
import java.io.*;

public class TCPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;


    public void start(int port) throws IOException{
       serverSocket = new ServerSocket(port);

       clientSocket = serverSocket.accept();
       out = new PrintWriter(clientSocket.getOutputStream(), true);
       in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedReader getIn() {
        return in;
    }

   /* public static void main(String[] args) {
        TCPServer server = new TCPServer();
        server.start(6666);
    } */
}
