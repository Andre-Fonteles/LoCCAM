package br.ufc.great.syssu.base.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleField;

public class MapTuple implements IMappable<Tuple> {

    private Tuple tuple;

    public MapTuple(Map<String, Object> map) {
    	this.tuple = fromMap(map);
    }

    public MapTuple(Tuple tuple) {
        this.tuple = tuple;
    }

    @Override
    public Map<String, Object> getMap() {
        return (tuple != null) ? toMap(tuple) : null;
    }
    
    @Override
    public Tuple getObject() {
    	return tuple;
    }

    private Tuple fromMap(Map<String, Object> map) {
        Tuple newTuple = new Tuple();
        for (Entry<String, Object> entry : map.entrySet()) {
            newTuple.addField(entry.getKey(), fromObject(entry.getValue()));
        }
        return newTuple;
    }

    private List<Object> fromList(List<Object> list) {
        List<Object> newList = new ArrayList<Object>();
        for (Object obj : list) {
            newList.add(fromObject(obj));
        }
        return newList;
    }

    @SuppressWarnings("unchecked")
	private Object fromObject(Object object) {
        if (object != null) {
            if (object instanceof Boolean || object instanceof Number || object instanceof String) {
                return object;
            } else if (object instanceof List) {
                return fromList((List<Object>) object);
            } else if (object instanceof Map) {
                return fromMap((Map<String, Object>) object);
            }
        }
        throw new IllegalArgumentException(
                "Invalid value type. Only Boolean, Number, String, List and Tuple are accepted.");
    }

    private Map<String, Object>  toMap(Tuple tuple) {
        Map<String, Object> newMap = new LinkedHashMap<String, Object>();
        for (int i = 0; i < tuple.size(); i++) {
            TupleField field = tuple.getField(i);
            newMap.put(field.getName(), toObject(field.getValue()));
        }
        return newMap;
    }

    private List<Object> toList(List<Object> list) {
        List<Object> newList = new ArrayList<Object>();
        for (Object object : list) {
            newList.add(toObject(object));
        }
        return newList;
    }

    @SuppressWarnings("unchecked")
	private Object toObject(Object object) {
        if (object != null) {
            if (object instanceof Boolean || object instanceof Number || object instanceof String) {
                return object;
            } else if (object instanceof List) {
                return toList((List<Object>) object);
            } else if (object instanceof Tuple) {
                return toMap((Tuple) object);
            }
        }
        throw new IllegalArgumentException(
                "Invalid value type. Only Boolean, Number, String, List and Tuple are accepted.");
    }

}
