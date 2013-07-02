package br.ufc.great.syssu.jsonrpc2;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class JSONRPC2Message {

    protected static final JSONParser parser = new JSONParser();

    public static JSONRPC2Message parse(final String jsonString)
            throws JSONRPC2ParseException, JSONRPC2InvalidMessageException {
        try {
            JSONObject json = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            throw new JSONRPC2ParseException("Invalid JSON: " + e.getMessage(), jsonString);
        }

        try { return JSONRPC2Request.parse(jsonString); } catch (Exception e) {}
        try { return JSONRPC2Notification.parse(jsonString); } catch (Exception e) {}
        try { return JSONRPC2Response.parse(jsonString); } catch (Exception e) {}
        try { return JSONRPC2Subscription.parse(jsonString); } catch (Exception e) {}

        throw new JSONRPC2InvalidMessageException("Invalid JSON-RPC 2.0 message", jsonString);
    }

    protected static JSONObject parseJSONObject(final String jsonString)
            throws JSONRPC2ParseException {
        if (jsonString == null) {
            throw new JSONRPC2ParseException("Null argument");
        }

        if (jsonString.trim().length() == 0) {
            throw new JSONRPC2ParseException("Invalid JSON: Empty string");
        }

        JSONObject json;
        try {
            json = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            String message = e.getMessage();
            if (message == null) {
                throw new JSONRPC2ParseException("Invalid JSON", jsonString);
            } else {
                throw new JSONRPC2ParseException("Invalid JSON: " + e.getMessage(), jsonString);
            }
        }

        return json;
    }

    protected static void ensureVersion2(final Object version, final String jsonString)
            throws JSONRPC2InvalidMessageException {
        if (version == null) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0: Version string missing", jsonString);
        } else if (!(version instanceof String)) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0: Version not a JSON string", jsonString);
        } else if (!version.equals("2.0")) {
            throw new JSONRPC2InvalidMessageException(
                    "Invalid JSON-RPC 2.0: Version must be \"2.0\"", jsonString);
        }
    }

    public abstract JSONObject toJSON();

    @Override
    public String toString() {
        return toJSON().toString();
    }
    
}
