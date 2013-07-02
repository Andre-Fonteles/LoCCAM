package br.ufc.great.syssu.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FieldUtils {
    public static String getType(Object value) {
        if (value != null) {
            if (value instanceof Boolean) {
                return "?boolean";
            } else if (value instanceof Integer || value instanceof Long) {
                return "?integer";
            } else if (value instanceof Float || value instanceof Double) {
                return "?float";
            } else if (value instanceof String) {
                return "?string";
            } else if (value instanceof List) {
                return "?array";
            } else if (value instanceof Tuple) {
                return "?object";
            }
        }
        throw new IllegalArgumentException(
                "Invalid value type. Only Boolean, Number, String, List and Tuple are accepted");
    }
    
    public static Object getDefaultValue(Object value) {
        if (value != null) {
            if (value instanceof Boolean) {
                return false;
            } else if (value instanceof Integer || value instanceof Long) {
                return 0;
            } else if (value instanceof Float || value instanceof Double) {
                return 0.0;
            } else if (value instanceof String) {
                return getDefaultValue((String)value);
            } else if (value instanceof List) {
                return new ArrayList<Object>();
            } else if (value instanceof Tuple) {
                return new LinkedHashMap<String, Object>();
            }
        }
        throw new IllegalArgumentException(
                "Invalid value type. Only Boolean, Number, String, List and Tuple are accepted");
    }
    
    private static Object getDefaultValue(String type) {
        if (type.equals("?boolean")) {
            return false;
        } else if (type.equals("?integer")) {
            return 0;
        } else if (type.equals("?float")) {
            return 0.0;
        } else if (type.equals("?string")) {
            return "";
        } else if (type.equals("?array")) {
            return new ArrayList<Object>();
        } else if (type.equals("?object")) {
            return new LinkedHashMap<String, Object>();
        } else {
        	return "";
        }
    }
}

