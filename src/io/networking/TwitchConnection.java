package io.networking;

import java.io.*;
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
    private String channelID;

    private BufferedReader inStream;
    private BufferedWriter outStream;

    private final BlockingQueue<String> messages = new ArrayBlockingQueue<>(500);
    private Socket SOCKET = null;
    private final Thread NETWORK_THREAD = new Thread(this);

    public TwitchConnection(String name, String oAuthToken, String channelID) {
        this.name = name;
        this.oAuthToken = oAuthToken;
        this.channelID = channelID;

        try {
            ADDRESS = InetAddress.getByName("irc.chat.twitch.tv");
        } catch (UnknownHostException e) {
            System.err.println("Can't resolve address of twitch server! Exiting...");
            return;
        }

        try {
            SOCKET = new Socket(ADDRESS, PORT);
            SOCKET.setSoTimeout(2000);
        } catch (IOException e) {
            System.err.println("Can't init socket connection to the twitch server! Exiting...");
            return;
        }

        try {
            this.outStream = new BufferedWriter(new OutputStreamWriter(SOCKET.getOutputStream()));
            this.inStream = new BufferedReader(new InputStreamReader(SOCKET.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        NETWORK_THREAD.setName("network-1");
        NETWORK_THREAD.setDaemon(true);
        NETWORK_THREAD.start();
    }

    private void initConnection() {
        try {
            String loginMsg = String.format("PASS %s\n\rNICK %s\n\r", oAuthToken, name);
            outStream.write(loginMsg);
            outStream.flush();

            while (!recvData().equals(""));

            String joinMsg = String.format("JOIN #%s\n", channelID);
            outStream.write(joinMsg);
            outStream.flush();
        } catch (IOException | InterruptedException e) {
            System.err.println("Can't send data to twitch server! Exiting...");
        }
    }

    private String recvData() throws InterruptedException {
        String data;
        try {
            String stringData = inStream.readLine();
            data = stringData;
            for (String s : stringData.split("\n")) {
                messages.put(s);
            }
        } catch (IOException e) {
            return "";
        }
        return data;
    }

    public BlockingQueue<String> getMessages() {
        return messages;
    }

    @Override
    public void run() {
        initConnection();
        while(!NETWORK_THREAD.isInterrupted()) {
            try {
                recvData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
