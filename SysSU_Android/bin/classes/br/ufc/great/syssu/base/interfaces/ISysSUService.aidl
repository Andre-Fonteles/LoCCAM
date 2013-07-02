package br.ufc.great.syssu.base.interfaces;

import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.IClientReaction;
import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;

// Declare the interface.
interface ISysSUService {
    
    void put(in Tuple tuple);
    List<Tuple> read(in Pattern pattern, IFilter filter);
	List<Tuple> take(in Pattern pattern, IFilter filter);
	void subscribe(in IClientReaction clientReaction, String event, in Pattern pattern, IFilter filter);
	
	String printLoCCAMState();
	
}