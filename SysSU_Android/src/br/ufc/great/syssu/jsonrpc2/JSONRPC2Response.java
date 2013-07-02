package br.ufc.great.syssu.jsonrpc2;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

public class JSONRPC2Response extends JSONRPC2Message {

    private Object result = null;
    private JSONRPC2Error error = null;
    private Object id = null;

    @SuppressWarnings("unchecked")
	public static JSONRPC2Response parse(String jsonString) 
            throws JSONRPC2ParseException, JSONRPC2InvalidMessageException {

        JSONObject json = JSONRPC2Message.parseJSONObject(jsonString);

        Object version = json.get("jsonrpc");
        ensureVersion2(version, jsonString);

        Object id = json.get("id");
        if (id != null
                && !(id instanceof Boolean)
                && !(id instanceof Number)
                && !(id instanceof String)) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 response: Identifier not a JSON scalar", jsonString);
        }

        if (json.containsKey("result") && !json.containsKey("error")) {
            Object res = json.get("result");
            return new JSONRPC2Response(res, id);

        } else if (!json.containsKey("result") && json.containsKey("error")) {
            Map<String, Object> errorJSON = (Map<String, Object>)json.get("error");
            if (errorJSON == null) {
                throw new JSONRPC2InvalidMessageException(
                        "Invalid JSON-RPC 2.0 response: Missing error object", jsonString);
            }
            
            int errorCode;
            try {
                errorCode = ((Long) errorJSON.get("code")).intValue();
            } catch (Exception e) {
                throw new JSONRPC2InvalidMessageException(
                        "Invalid JSON-RPC 2.0 response: Error code missing or not an integer",
                        jsonString);
            }

            String errorMessage = null;
            try {
                errorMessage = (String) errorJSON.get("message");
            } catch (Exception e) {
                throw new JSONRPC2InvalidMessageException(
                        "Invalid JSON-RPC 2.0 response: Error message missing or not a string",
                        jsonString);
            }

            Object errorData = errorJSON.get("data");

            return new JSONRPC2Response(new JSONRPC2Error(errorCode, errorMessage, errorData), id);

        } else if (json.containsKey("result") && json.containsKey("error")) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 response: You cannot have result and error at the same time"
                    , jsonString);
        } else if (!json.containsKey("result") && !json.containsKey("error")) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0 response: Neither result nor error specified"
                    , jsonString);
        } else {
            throw new AssertionError();
        }
        
    }

    public JSONRPC2Response(Object result, Object id) {
        setResult(result);
        setID(id);
    }

    public JSONRPC2Response(JSONRPC2Error error, Object id) {
        setError(error);
        setID(id);
    }

    public void setResult(Object result) {
        if (result != null
                && !(result instanceof Boolean)
                && !(result instanceof Number)
                && !(result instanceof String)
                && !(result instanceof List)
                && !(result instanceof Map)) {
            throw new IllegalArgumentException("The result must map to a JSON type");
        }

        this.result = result;
        this.error = null;
    }

    public Object getResult() {
        return result;
    }

    public void setError(JSONRPC2Error error) {
        if (error == null) {
            throw new NullPointerException("The error object cannot be null");
        }
        this.error = error;
        this.result = null;
    }

    public JSONRPC2Error getError() {
        return error;
    }

    public boolean indicatesSuccess() {
        if (error == null) {
            return true;
        } else {
            return false;
        }
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

    public Object getID() {
        return id;
    }

    public JSONObject toJSON() {
        JSONObject out = new JSONObject();

        if (error != null) {
            out.put("error", error.toJSON());
        } else {
            out.put("result", result);
        }
        out.put("id", id);
        out.put("jsonrpc", "2.0");

        return out;
    }
    
}
