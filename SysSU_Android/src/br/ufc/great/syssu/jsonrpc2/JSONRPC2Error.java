package br.ufc.great.syssu.jsonrpc2;

import org.json.simple.JSONObject;

@SuppressWarnings("serial")
public class JSONRPC2Error extends Exception {

	public static final JSONRPC2Error PARSE_ERROR = new JSONRPC2Error(-32700, "JSON parse error");
	public static final JSONRPC2Error INVALID_REQUEST = new JSONRPC2Error(-32600, "Invalid request");
	public static final JSONRPC2Error METHOD_NOT_FOUND = new JSONRPC2Error(-32601, "Method not found");
	public static final JSONRPC2Error INVALID_PARAMS = new JSONRPC2Error(-32602, "Invalid parameters");
	public static final JSONRPC2Error INTERNAL_ERROR = new JSONRPC2Error(-32603, "Internal error");

	private int code;
	private Object data;

	public JSONRPC2Error(int code, String message) {
		super(message);
		this.code = code;
	}

	public JSONRPC2Error(int code, String message, Object data) {
		super(message);
		this.code = code;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public JSONObject toJSON() {
		JSONObject out = new JSONObject();

		out.put("code", code);
		out.put("message", super.getMessage());
		if (data != null) {
			out.put("data", data);
		}

		return out;
	}

	public String toString() {
		return toJSON().toString();
	}

}
