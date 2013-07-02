package br.ufc.great.syssu.net;

import java.net.InetAddress;

public class NetworkMessageReceived {
    private InetAddress inetAddres;
    private String message;
    private int port;

    public NetworkMessageReceived(InetAddress inetAddres, int port, String message) {
        this.inetAddres = inetAddres;
        this.message = message;
        this.port = port;
    }

    public InetAddress getInetAddres() {
        return this.inetAddres;
    }

    public String getMessage() {
        return this.message;
    }

    public int getPort() {
        return this.port;
    }
}

