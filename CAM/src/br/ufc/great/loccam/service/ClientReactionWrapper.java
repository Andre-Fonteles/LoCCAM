package br.ufc.great.loccam.service;

import android.os.RemoteException;
import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IClientReaction;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.IReaction;

public class ClientReactionWrapper implements IReaction{

	private IClientReaction clientReaction;
	
    private Pattern pattern;
    private IFilter filter;
    private Object id;
	
	public ClientReactionWrapper(IClientReaction clientReaction, Pattern pattern, IFilter filter) {
		this.clientReaction = clientReaction;

		this.pattern = pattern;
        this.filter = filter;
	}
	
	public void setId(Object id) {
    	this.id = id;
	}

	public Object getId() {
		return id;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public String getJavaScriptFilter() {
		return null;
	}

	public IFilter getJavaFilter() {
		return filter;
	}

	public void react(Tuple tuple) throws RemoteException {
		clientReaction.react(tuple);
	}

}
