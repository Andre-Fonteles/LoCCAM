package br.ufc.great.syssu.jsonrpc2;

public class JSONRPC2ParseException extends Exception {

    private String unparsableString = null;

    public JSONRPC2ParseException(String message) {
        super(message);
    }

    public JSONRPC2ParseException(String message, String unparsableString) {
        super(message);
        this.unparsableString = unparsableString;
    }

    public String getUnparsableString() {
        return unparsableString;
    }

}
