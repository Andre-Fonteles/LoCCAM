package br.ufc.great.syssu.net;

import java.io.*;
import java.net.*;

public class TCPNetworkClient {

    private static final String CHAR_SET = "UTF8";
    private static final int END_OF_TRANSMITION = 4;

    private String address;
    private int port;

    public TCPNetworkClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String sendMessage(String message) throws IOException {
        String result = null;
        Socket socket = null;
        Writer writer = null;
        InputStream is = null;
        try {
            socket = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(this.address, this.port);
            socket.connect(sockaddr, 3000);

            is = socket.getInputStream();
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), CHAR_SET));
            writer.write(message);
            writer.write(END_OF_TRANSMITION);
            writer.flush();
            result = convertStreamToString(is);

            return result;
        } finally {
            socket.close();
        }
    }

    private String convertStreamToString(InputStream is) throws IOException {
        String result = null;
        if (is != null) {
            Writer writer = new StringWriter();
            Reader reader = new InputStreamReader(is, "UTF8");
            int c;
            while ((c = reader.read()) != 4) {
                writer.append((char) c);
            }
            result = writer.toString();
        }
        return result;
    }
}
