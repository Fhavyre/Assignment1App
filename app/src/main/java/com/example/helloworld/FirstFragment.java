package com.example.helloworld;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.helloworld.databinding.FragmentFirstBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private TCPClient tcpClient;
    private String ipAddress;
    private int port;
    private String message;
    private TCPServer tcpServer;
    private ScrollView scroller;
    private TextView messageView;
    private String chat;
    private static boolean tcpServerUp;
    private static boolean tcpClientUp;
    private static boolean udpServerUp;
    private static boolean udpClientUp;
    private View view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        binding = FragmentFirstBinding.inflate(inflater, container, false);
        scroller = binding.messages;
        messageView = binding.messageView;
        chat = "";
        tcpServerUp = false;
        tcpClientUp = false;
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity.isTcp()) {
                    tcpThreads();
                }else if(activity.isUdp()) {
                    udpThreads();
                }

            }
        });
    }

    private void tcpThreads() {
        //setup Client
        if(!tcpClientUp) {
            new Thread(() -> {
                try {
                    setupClient();
                    tcpClientUp = true;
                } catch(IOException e) {
                    Snackbar.make(view, "Could not establish connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }).start();
        }

        //setup Server
        if(!tcpServerUp) {
            new Thread(() -> {
                try {
                    setupServer();
                } catch (IOException e) {
                    Snackbar.make(view, "Could not establish connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }).start();
        }

        //wait for incoming messages
        new Thread(() -> {
            try {
                waitForMessages();
            } catch (IOException e) {
                Snackbar.make(view, "Could not establish connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }).start();

        //send messages
        new Thread(() -> {
            try {
                while(!tcpClientUp) {
                    Log.i("client", "warte auf clientSocket");
                }
                Log.i("client", "bin jetzt raus");
                sendMessage();
                //append sent message to chat
                getActivity().runOnUiThread( ()-> {
                    chat += "You: " + message + "\n";
                    binding.messageView.setText(chat);
                    binding.messageField.setText("");
                    scrollDown();
                });
            } catch (IOException e) {
                Snackbar.make(view, "A Network Error occurred", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } catch (NullPointerException e) {
                Snackbar.make(view, "Please fill out all Information. Message, can not be empty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void udpThreads() {
        new Thread(() -> {
            if(!udpServerUp)  {

            }
        });
    }

    /**
     * scrolls the messageView to the bottom
     */
    public void scrollDown() {
        scroller.smoothScrollTo(0, messageView.getBottom());
    }

    /**
     * start Client side to be able to send messages
     */
    private void setupClient() throws IOException{
        tcpClient = new TCPClient();
        ipAddress = binding.ipAddress.getText().toString();
        port = Integer.parseInt(binding.port.getText().toString());
        tcpClient.startConnection(ipAddress, port);
    }

    /**
     * start server side to be able to receive messages
     */
    private void setupServer() throws IOException {
        tcpServer = new TCPServer();
        tcpServer.start(1234);
    }

    /**
     *
     */
    private void sendMessage() throws IOException, NullPointerException, InterruptedException {
        message = binding.messageField.getText().toString();
        tcpClient.sendMessage(message);
    }

    private void waitForMessages() throws IOException {
        String input;
        while (tcpServer == null) {

        }

        while (tcpServer.getIn() == null) {
            Log.i("wait", "waiting for Instream");
        }

        while ((input = tcpServer.getIn().readLine()) != null) {
            if (".".equals(input)) {
                break;
            }
            chat += "Server:" + input + "\n";
            binding.messageView.setText(chat);
        }

        //if . gets send close connection.
        tcpServer.stop();
        tcpClient.stopConnection();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}