package br.ufc.great.syssu.jsonrpc2;

import java.util.*;
import org.json.simple.*;

public class JSONRPC2Subscription extends JSONRPC2Message {

    private String event;
    private int port;
    private Object params;
    private JSONRPC2ParamsType paramsType;
    private Object id;

    public static JSONRPC2Subscription parse(final String jsonString)
            throws JSONRPC2ParseException, JSONRPC2InvalidMessageException {
        JSONObject json = JSONRPC2Message.parseJSONObject(jsonString);

        Object version = json.get("jsonrpc");
        ensureVersion2(version, jsonString);

        Object event = json.get("event");

        if (event == null) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 event: Event name missing", jsonString);
        } else if (!(event instanceof String)) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 event: Event name not a JSON string", jsonString);
        } else if (((String) event).length() == 0) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 event: Event name is an empty string", jsonString);
        }

        if (!json.containsKey("id")) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 event: Missing identifier", jsonString);
        }

        Object id = json.get("id");       
        
        if (id != null
                && !(id instanceof Number)
                && !(id instanceof Boolean)
                && !(id instanceof String)) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 event: Identifier not a JSON scalar", jsonString);
        }
        
        if (!json.containsKey("port")) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 event: Missing port", jsonString);
        }
        
        int	port = Integer.parseInt(json.get("port").toString());

        Object params = json.get("params");

        if (params == null) {
            return new JSONRPC2Subscription((String) event, port, id);
        } else if (params instanceof List) {
            return new JSONRPC2Subscription((String) event, port, (List) params, id);
        } else if (params instanceof Map) {
            return new JSONRPC2Subscription((String) event, port, (Map) params, id);
        } else {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 event: Event parameters have unexpected JSON type",
                    jsonString);
        }
    }

    public JSONRPC2Subscription(String event, int port, Object id) {
        setEvent(event);
        setPort(port);
        setParams(null);
        setID(id);
    }

    public JSONRPC2Subscription(String event, int port, List params, Object id) {
        setEvent(event);
        setPort(port);
        setParams(params);
        setID(id);
    }

    public JSONRPC2Subscription(String event, int port, Map params, Object id) {
        setEvent(event);
        setPort(port);
        setParams(params);
        setID(id);
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        if (event == null) {
            throw new NullPointerException();
        }
        this.event = event;
    }
    
    public void setPort(int port) {
    	this.port = port;
    }
    
    public int getPort() {
    	return this.port;
    }

    public JSONRPC2ParamsType getParamsType() {
        return paramsType;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        if (params == null) {
            paramsType = JSONRPC2ParamsType.NO_PARAMS;
        } else if (params instanceof List) {
            paramsType = JSONRPC2ParamsType.ARRAY;
        } else if (params instanceof Map) {
            paramsType = JSONRPC2ParamsType.OBJECT;
        } else {
            throw new IllegalArgumentException("The event parameters must be of type List, Map or null");
        }
        this.params = params;
    }

    public Object getID() {
        return id;
    }

    public void setID(Object id) {
        if (id != null
                && !(id instanceof Boolean)
                && !(id instanceof Number)
                && !(id instanceof String)) {
            throw new IllegalArgumentException("The event identifier must map to a JSON scalar");
        }
        this.id = id;
    }

    public JSONObject toJSON() {
        JSONObject req = new JSONObject();

        req.put("event", event);
        if (params != null && paramsType != JSONRPC2ParamsType.NO_PARAMS) {
            req.put("params", params);
        }
        req.put("port", port);
        req.put("id", id);
        req.put("jsonrpc", "2.0");

        return req;
    }
    
}
