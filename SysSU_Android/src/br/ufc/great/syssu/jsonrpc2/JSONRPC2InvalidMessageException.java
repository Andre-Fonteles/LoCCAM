package br.ufc.great.syssu.jsonrpc2;

public class JSONRPC2InvalidMessageException extends Exception {

    private String messageString = null;

    public JSONRPC2InvalidMessageException(String message) {
        super(message);
    }

    public JSONRPC2InvalidMessageException(String message, String messageString) {
        super(message);
        this.messageString = messageString;
    }

    public String getUnparsableString() {
        return messageString;
    }

}

