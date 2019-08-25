package io.networking;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TwitchConnection implements Runnable {
    private InetAddress ADDRESS = null;
    private final int PORT = 6667;

    private String name;
    private String oAuthToken;

    private final int PACKET_SIZE = (int)(Math.pow(2, 16));

    private final BlockingQueue<String> messages = new ArrayBlockingQueue<>(500);
    private Socket SOCKET = null;
    private final Thread NETWORK_THREAD = new Thread(this);

    public TwitchConnection(String name, String oAuthToken) {
        this.name = name;
        this.oAuthToken = oAuthToken;

        try {
            ADDRESS = InetAddress.getByName("irc.chat.twitch.tv");
        } catch (UnknownHostException e) {
            System.err.println("Can't resolve address of twitch server! Exiting...");
            // TODO: maybe close program here
            return;
        }

        try {
            SOCKET = new Socket(ADDRESS, PORT);
        } catch (IOException e) {
            System.err.println("Can't init socket connection to the twitch server! Exiting...");
            // TODO: maybe close program here
            return;
        }

        NETWORK_THREAD.setName("network-1");
        NETWORK_THREAD.setDaemon(true);
        NETWORK_THREAD.start();
    }

    private void initConnection() {
        try (BufferedOutputStream bStream = new BufferedOutputStream(SOCKET.getOutputStream())) {
            String loginMsg = String.format("PASS %s\nNICK %s\n", oAuthToken, name);
            byte[] data = loginMsg.getBytes();

            bStream.write(data, 0, data.length);
        } catch (IOException e) {
            System.err.println("Can't send data to twitch server! Exiting...");
            try {
                SOCKET.close();
            } catch (IOException ex) {
            }
        }
    }

    private void recvData() throws InterruptedException {
        try (BufferedInputStream bStream = new BufferedInputStream(SOCKET.getInputStream())) {
            byte[] data = new byte[PACKET_SIZE];
            bStream.read(data, 0, data.length);

            String stringData = new String(data);
            for (String s : stringData.split("\n")) {
                messages.put(s);
            }
        } catch (IOException e) {
            System.err.println("Can't send data to twitch server! Exiting...");
            try {
                SOCKET.close();
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public void run() {
        initConnection();
        while(!NETWORK_THREAD.isInterrupted()) {
            try {
                recvData();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
