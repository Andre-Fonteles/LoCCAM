package br.ufc.great.syssu.ubibroker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.interfaces.IReaction;
import br.ufc.great.syssu.base.utils.MapTuple;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Message;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Response;
import br.ufc.great.syssu.net.NetworkMessageReceived;
import br.ufc.great.syssu.net.TCPNetwokServer;
import br.ufc.great.syssu.net.TCPNetworkClient;
import br.ufc.great.syssu.net.interfaces.INetworkObserver;

public class UbiBroker implements INetworkObserver, Runnable {

    private TCPNetworkClient tcpClient;
    private TCPNetwokServer tcpServer;
    private List<IReaction> reactions;
    private int reactionsPort;

    private UbiBroker(String ubicentreAddress, int ubiCentrePort, int reactionsPort) throws IOException {
        this.tcpClient = new TCPNetworkClient(ubicentreAddress, ubiCentrePort);
        this.tcpServer = new TCPNetwokServer(reactionsPort);
        this.reactions = new ArrayList<IReaction>();
        this.reactionsPort = reactionsPort;
    }

    public static UbiBroker createUbibroker(String ubicentreAddress, int ubicentrePort, int reactionsPort) 
    		throws IOException {
        UbiBroker instance = new UbiBroker(ubicentreAddress, ubicentrePort, reactionsPort);
        new Thread(instance).start();
        return instance;
    }
    
    public IDomain getDomain(String name) throws TupleSpaceException {
        return new Domain(name, this);
    }

    int getReactionsPort() {
        return reactionsPort;
    }

    String sendMessage(String message) throws IOException {
        return tcpClient.sendMessage(message);
    }

    void addReaction(IReaction reaction) throws TupleSpaceException {
        reactions.add(reaction);
    }

    @SuppressWarnings("unchecked")
	@Override
    public String process(NetworkMessageReceived message) {
        JSONRPC2Message msn = null;

        try {
            msn = JSONRPC2Message.parse(message.getMessage());
            if (msn instanceof JSONRPC2Response) {
                JSONRPC2Response response = (JSONRPC2Response) msn;
                if (response.indicatesSuccess()) {
                    Object res = response.getResult();
                    if (res instanceof Map) {
                        for (IReaction reaction : reactions) {
                            if (response.getID().equals(reaction.getId())) {
                                reaction.react(new MapTuple((Map<String, Object>) res).getObject());
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }

        return null;
    }

    @Override
    public void run() {
        try {
            tcpServer.setNetworkObserver(this);
            tcpServer.start();
        } catch (IOException ex) {
        }
    }
}
