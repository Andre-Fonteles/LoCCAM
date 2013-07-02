package br.ufc.great.syssu.base.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.PatternField;

public class MapPattern implements IMappable<Pattern> {

    private Pattern pattern;

    public MapPattern(Map<String, Object> map) {
    	this.pattern = fromMap(map);
    }

    public MapPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Map<String, Object> getMap() {
        return (pattern != null) ? toMap(pattern) : null;
    }
    
    @Override
    public Pattern getObject() {
        return pattern;
    }

    private Pattern fromMap(Map<String, Object> map) {
        Pattern newPattern = new Pattern();
        for (Entry<String, Object> entry : map.entrySet()) {
            newPattern.addField(entry.getKey(), fromObject(entry.getValue()));
        }
        return newPattern;
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

    private Map<String, Object> toMap(Pattern pattern) {
        Map<String, Object> newMap = new LinkedHashMap<String, Object>();
        for (int i = 0; i < pattern.size(); i++) {
            PatternField field = pattern.getField(i);
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
            } else if (object instanceof Pattern) {
                return toMap((Pattern) object);
            }
        }
        throw new IllegalArgumentException(
                "Invalid value type. Only Boolean, Number, String, List and Tuple are accepted.");
    }
}
