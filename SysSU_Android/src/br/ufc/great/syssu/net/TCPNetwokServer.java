package br.ufc.great.syssu.net;

import br.ufc.great.syssu.net.interfaces.*;
import java.net.*;
import java.io.*;

public class TCPNetwokServer implements INetworkServer {

    private ServerSocket serverSocket;
    private INetworkObserver networkObserver;
    private boolean stopped;

    public TCPNetwokServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        setStopped(false);
    }

    private void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
    
    public int getPort() {
    	return serverSocket.getLocalPort();
    }

    @Override
    public void setNetworkObserver(INetworkObserver observer) {
        this.networkObserver = observer;
    }

    @Override
    public INetworkObserver getNetworkObserver() {
        return this.networkObserver;
    }

    @Override
    public void start() throws IOException {
        if(this.networkObserver == null) {
            throw new IllegalStateException("No network observer was defined.");
        }
        while(!isStopped()) {         
            try {
                Socket clientSocket = this.serverSocket.accept();
                new Thread(new ClientWorker(this.networkObserver, clientSocket)).start();
            } catch (IOException e) {
                if(!isStopped()) {
                    throw new IOException(
                        "Error accepting client connection", e);
                }                
            }            
        }
    }

    @Override
    public void stop() throws IOException{
        setStopped(true);
        this.serverSocket.close();
    }

}

