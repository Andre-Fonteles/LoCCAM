package br.ufc.great.syssu.jsonrpc2;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class JSONRPC2Notification extends JSONRPC2Message {

    private String method;
    private Object params;
    private JSONRPC2ParamsType paramsType;

    public static JSONRPC2Notification parse(final String jsonString) 
            throws JSONRPC2ParseException, JSONRPC2InvalidMessageException {
        JSONObject json = JSONRPC2Message.parseJSONObject(jsonString);

        Object version = json.get("jsonrpc");
        ensureVersion2(version, jsonString);

        Object method = json.get("method");

        if (method == null) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 notification: Method name missing", jsonString);
        } else if (!(method instanceof String)) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 notification: Method name not a JSON string", jsonString);
        } else if (((String) method).length() == 0) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 notification: Method name is an empty string", jsonString);
        }

        Object params = json.get("params");

        if (params == null) {
            return new JSONRPC2Notification((String) method);
        } else if (params instanceof List) {
            return new JSONRPC2Notification((String) method, (List) params);
        } else if (params instanceof Map) {
            return new JSONRPC2Notification((String) method, (Map) params);
        } else {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 notification: Method parameters have unexpected JSON type",
                    jsonString);
        }
    }

    public JSONRPC2Notification(String method) {
        setMethod(method);
        setParams(null);
    }

    public JSONRPC2Notification(String method, List params) {
        setMethod(method);
        setParams(params);
    }

    public JSONRPC2Notification(String method, Map params) {
        setMethod(method);
        setParams(params);
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
            throw new IllegalArgumentException("The notification parameters must be of type List, Map or null");
        }
        this.params = params;
    }

    public JSONObject toJSON() {
        JSONObject req = new JSONObject();

        req.put("method", method);
        if (params != null && paramsType != JSONRPC2ParamsType.NO_PARAMS) {
            req.put("params", params);
        }
        req.put("jsonrpc", "2.0");

        return req;
    }

}
