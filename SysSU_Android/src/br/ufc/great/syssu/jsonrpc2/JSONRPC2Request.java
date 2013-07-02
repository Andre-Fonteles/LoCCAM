package br.ufc.great.syssu.jsonrpc2;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class JSONRPC2Request extends JSONRPC2Message {

    private String method;
    private Object params;
    private JSONRPC2ParamsType paramsType;
    private Object id;

    public static JSONRPC2Request parse(final String jsonString)
            throws JSONRPC2ParseException, JSONRPC2InvalidMessageException {

        JSONObject json = JSONRPC2Message.parseJSONObject(jsonString);

        Object version = json.get("jsonrpc");
        ensureVersion2(version, jsonString);

        Object method = json.get("method");

        if (method == null) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 request: Method name missing", jsonString);
        } else if (!(method instanceof String)) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 request: Method name not a JSON string", jsonString);
        } else if (((String) method).length() == 0) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 request: Method name is an empty string", jsonString);
        }

        if (!json.containsKey("id")) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 request: Missing identifier", jsonString);
        }

        Object id = json.get("id");

        if (id != null
                && !(id instanceof Number)
                && !(id instanceof Boolean)
                && !(id instanceof String)) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 request: Identifier not a JSON scalar", jsonString);
        }

        Object params = json.get("params");

        if (params == null) {
            return new JSONRPC2Request((String) method, id);
        } else if (params instanceof List) {
            return new JSONRPC2Request((String) method, (List) params, id);
        } else if (params instanceof Map) {
            return new JSONRPC2Request((String) method, (Map) params, id);
        } else {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 request: Method parameters have unexpected JSON type",
                    jsonString);
        }
        
    }

    public JSONRPC2Request(String method, Object id) {
        setMethod(method);
        setParams(null);
        setID(id);
    }

    public JSONRPC2Request(String method, List params, Object id) {
        setMethod(method);
        setParams(params);
        setID(id);
    }

    public JSONRPC2Request(String method, Map params, Object id) {
        setMethod(method);
        setParams(params);
        setID(id);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        if (method == null) {
            throw new NullPointerException();
        }
        this.method = method;
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
            throw new IllegalArgumentException(
                    "The request parameters must be of type List, Map or null");
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
            throw new IllegalArgumentException("The request identifier must map to a JSON scalar");
        }
        this.id = id;
    }

    public JSONObject toJSON() {
        JSONObject req = new JSONObject();
        
        req.put("method", method);
        if (params != null && paramsType != JSONRPC2ParamsType.NO_PARAMS) {
            req.put("params", params);
        }
        req.put("id", id);
        req.put("jsonrpc", "2.0");

        return req;
    }

}
