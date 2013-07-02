package br.ufc.great.syssu.base;

import java.util.List;

public abstract class AbstractField {

	public abstract String getName();

	public abstract String getType();

	public abstract Object getValue();

	protected void verify(String name, Object value) throws IllegalArgumentException {
        if (name == null || name.equals("") || name.indexOf(".") != -1) {
            throw new IllegalArgumentException(
                    "Invalid field name. Field name can not be empty or contains '.'");
        }

        if (value == null || (value != null && !(
                   value instanceof Boolean
                || value instanceof Number
                || value instanceof String
                || value instanceof List
                || value instanceof Tuple))) {
            throw new IllegalArgumentException(
                    "Invalid value type. Only Boolean, Number, String, List and Tuple are accepted.");
        }
    } 
}
