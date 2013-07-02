package br.ufc.great.syssu.base.utils;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.ufc.great.syssu.base.Pattern;

public class JSONPattern implements JSONable<Pattern> {

    private Pattern pattern;

    public JSONPattern(String json) {
    	this.pattern = fromJSON(json);
    }

    public JSONPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public String getJSON() {
        return pattern != null ? JSONObject.toJSONString(new MapPattern(pattern).getMap()) : null;
    }

    @Override
    public Pattern getObject() {
        return pattern;
    }
    
    @SuppressWarnings("unchecked")
	private Pattern fromJSON(String json) {
        try {
            Object jsonObj = new JSONParser().parse(json);
            return new MapPattern((Map<String, Object>)jsonObj).getObject();
        } catch (ParseException ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }
}
