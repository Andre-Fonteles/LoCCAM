package br.ufc.great.loccam.filter;

import java.util.List;

import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;

public class ContainsStringFilter extends IFilter.Stub {
	
	private String contextKey;
	private String value;
	private int valueIndex;
	
	public ContainsStringFilter(String contextKey, String value) {
		this(contextKey, value, 0);
	}
	
	public ContainsStringFilter(String contextKey, String value, int valueIndex) {
		this.contextKey = contextKey;
		this.value = value;
		this.valueIndex = valueIndex;
	}
	
	public boolean filter(Tuple tuple) {
		if(constainsContextKey(tuple, contextKey)) {
			for (int i = 0; i < tuple.size(); i++) {
				if(tuple.getField(i).getName().equals("Values")) {
	
					@SuppressWarnings("unchecked")
					List<String> stringValues = (List<String>)tuple.getField(i).getValue();
					String tupleValue = stringValues.get(valueIndex);
					
					if(tupleValue.contains(value))
						return true;
					else
						return false;
				}
			}
		}
		return false;
	}
	
	public static boolean constainsContextKey(Tuple tuple, String key) {
		for (int i = 0; i < tuple.size(); i++) {
			if(tuple.getField(i).getName().equals("ContextKey")) {
				String tupleValue = (String)tuple.getField(i).getValue();
				if(tupleValue.equals(key))
					return true;
				else
					return false;
			}
		}
		return false;
	}
}

