package com.example.helloworld;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 *
 */
public class ExampleUnitTest {
    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        TCPClient client = new TCPClient();
        String response = "";
        try {
            client.startConnection("192.168.0.186", 6666);
            //response = client.sendMessage("hello server");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("hello client", response);
    }
}