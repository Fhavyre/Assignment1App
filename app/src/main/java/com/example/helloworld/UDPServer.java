package com.example.helloworld;

import java.io.IOException;
import java.net.*;

public class UDPServer {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buffer = new byte[256];

    public void start() throws SocketException {
        socket = new DatagramSocket();
    }

    public void run() throws IOException {
        running = true;

        while (running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buffer, buffer.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());

            if (received.equals(".")) {
                running = false;
                continue;
            }

            socket.send(packet);
        }
        socket.close();
    }
}
