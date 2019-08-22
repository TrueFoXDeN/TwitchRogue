package io.networking;

public class TwitchConnection {

    private final String IP = "";
    private final int PORT = 0;

    private String name;
    private String oAuthToken;

    public TwitchConnection(String name, String oAuthToken) {
        this.name = name;
        this.oAuthToken = oAuthToken;
    }
}
