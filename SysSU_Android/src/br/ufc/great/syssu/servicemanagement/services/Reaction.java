package br.ufc.great.syssu.servicemanagement.services;

import java.io.IOException;
import java.net.InetAddress;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.IReaction;
import br.ufc.great.syssu.base.utils.MapTuple;
import br.ufc.great.syssu.jsonrpc2.JSONRPC2Response;
import br.ufc.great.syssu.net.TCPNetworkClient;

public class Reaction implements IReaction {

    private Pattern pattern;
    private String restriction;
    private Object id;
    private InetAddress address;
    private int port;

    public Reaction(Pattern pattern, String restriction, InetAddress address, int port) {
        this.pattern = pattern;
        this.restriction = restriction;
        this.address = address;
        this.port = port;
    }
    
    @Override
    public void setId(Object id) {
    	this.id = id;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Pattern getPattern() {
        return pattern;
    }
    
    @Override
    public String getJavaScriptFilter() {
    	return restriction;
    }
    
    @Override
    public void react(Tuple tuple) {
        TCPNetworkClient client = new TCPNetworkClient(address.getHostAddress(), port);
        JSONRPC2Response response = new JSONRPC2Response(new MapTuple(tuple).getMap(), id);
        try {
            client.sendMessage(response.toJSON().toJSONString());
        } catch (IOException ex) {
        }
    }

	@Override
	public IFilter getJavaFilter() {
		// TODO Auto-generated method stub
		return null;
	}
}
