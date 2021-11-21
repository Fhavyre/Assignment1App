package com.example.helloworld;

import java.io.IOException;
import java.net.*;

public class UDPClient {

    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buffer;

    public void start() throws SocketException {
        socket = new DatagramSocket();
    }

    public void sendMessage(String ipAddress, int port, String message) throws IOException {
        InetAddress address = InetAddress.getByName(ipAddress);
        buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
        socket.send(packet);
    }

    public void close() {
        socket.close();
    }
}
