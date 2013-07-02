package br.ufc.great.syssu.net;

import br.ufc.great.syssu.net.interfaces.*;
import java.io.*;
import java.net.*;

class ClientWorker implements Runnable {
    private static final String CHAR_SET = "UTF8";
    private static final int END_OF_TRANSMITION = 4;

    private INetworkObserver observer;
    private Socket clientSocket;

    ClientWorker(INetworkObserver observer, Socket clientSocket) {
        this.observer = observer;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = this.clientSocket.getInputStream();
            out = this.clientSocket.getOutputStream();

            String message = convertStreamToString(in);
            
            String response = this.observer.process(new NetworkMessageReceived(
                    this.clientSocket.getInetAddress(), this.clientSocket.getPort(), message));

            if(response != null) {
                out.write(response.getBytes());
            }
            
            out.write(END_OF_TRANSMITION);
            out.flush();
        } catch (IOException ex) {
        } finally {
            try {
                this.clientSocket.close();
            } catch (IOException ex) { }
        }
    }

    private String convertStreamToString(InputStream is) throws IOException {
        String result = null;
        if (is != null) {
            Writer writer = new StringWriter();
            Reader reader = new BufferedReader(new InputStreamReader(is, CHAR_SET));
            int c = -1;
            while ((c = reader.read()) != END_OF_TRANSMITION) {
                writer.append((char) c);
            }
            result = writer.toString();
        }
        return result;
    }

}

