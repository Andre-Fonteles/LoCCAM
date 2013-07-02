package br.ufc.great.syssu.base.interfaces;

import android.os.RemoteException;
import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;

public interface IReaction {

	void setId(Object id);

	Object getId();

	Pattern getPattern();
	
	String getJavaScriptFilter();
	
	IFilter getJavaFilter();

	void react(Tuple tuple) throws RemoteException ;
}
