package br.ufc.great.loccam.filter;

import java.security.InvalidParameterException;
import java.util.List;

import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.interfaces.IFilter;

public class NumericFilter extends IFilter.Stub {

	private String contextKey;
	private String operator;
	private double value;
	private int valueIndex;

	public NumericFilter(String contextKey, String operator, double value) throws InvalidParameterException {
		this(contextKey, operator, value, 0);
	}

	public NumericFilter(String contextKey, String operator, double value, int valueIndex) throws InvalidParameterException {
		this.contextKey = contextKey;
		this.operator = operator;
		this.value = value;
		this.valueIndex = valueIndex;

		if (!(operator.equals(">") || operator.equals(">=")
				|| operator.equals("<") || operator.equals("<=")))
			throw new InvalidParameterException();
	}

	public boolean filter(Tuple tuple) {
		if (constainsContextKey(tuple, contextKey)) {
			for (int i = 0; i < tuple.size(); i++) {
				if (tuple.getField(i).getName().equals("Values")) {

					@SuppressWarnings("unchecked")
					List<String> stringValues = (List<String>) tuple.getField(i).getValue();
					double tupleValue = Double.parseDouble(stringValues.get(valueIndex));

					if (operator.equals(">") && tupleValue > value)
						return true;
					else if (operator.equals(">=") && tupleValue >= value)
						return true;
					else if (operator.equals("<") && tupleValue < value)
						return true;
					else if (operator.equals("<=") && tupleValue <= value)
						return true;
				}
			}
		}
		return false;
	}

	public static boolean constainsContextKey(Tuple tuple, String key) {
		for (int i = 0; i < tuple.size(); i++) {
			if (tuple.getField(i).getName().equals("ContextKey")) {
				String tupleValue = (String) tuple.getField(i).getValue();
				if (tupleValue.equals(key))
					return true;
				else
					return false;
			}
		}
		return false;
	}
}
