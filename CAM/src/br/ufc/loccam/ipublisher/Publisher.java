package br.ufc.loccam.ipublisher;

import java.io.IOException;
import java.util.List;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.interfaces.IReaction;
import br.ufc.great.syssu.ubibroker.LocalUbiBroker;

public class Publisher implements IPublisher{

	private LocalUbiBroker localUbiBroker;
	private IDomain domain;
	
	public Publisher(){
		try {
			localUbiBroker = LocalUbiBroker.createUbibroker();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			domain = localUbiBroker.getDomain("loccam");
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	public void put(String contextData, String source, List<String> values, String accuracy, String timestamp) {		
		// Retira a tupla com o valor anterior
		Pattern pattern = (Pattern) new Pattern()
		.addField("ContextKey", contextData)
		.addField("Source", "?")
		.addField("Values", "?")
		.addField("Accuracy", "?")
		.addField("Timestamp", "?");
	
		try {
			domain.take(pattern, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Tuple tuple = (Tuple) new Tuple()
			.addField("ContextKey", contextData)
			.addField("Source", source)
			.addField("Values", values)
			.addField("Accuracy", accuracy)
			.addField("Timestamp", timestamp);
		
		try {
			domain.put(tuple, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Tuple> read(String contextData) {
		Pattern pattern = (Pattern) new Pattern()
		.addField("ContextKey", contextData)
		.addField("Source", "?")
		.addField("Values", "?")
		.addField("Accuracy", "?")
		.addField("Timestamp", "?");
	
		List<Tuple> list = null;

		try {
			list = domain.read(pattern, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public void subscribe(IReaction reaction, String event, String key) {
		try {
			reaction.setId(domain.subscribe(reaction, event, key));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
