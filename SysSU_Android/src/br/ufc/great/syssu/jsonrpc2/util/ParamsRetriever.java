package br.ufc.great.syssu.jsonrpc2.util;

import br.ufc.great.syssu.jsonrpc2.JSONRPC2Error;

public abstract class ParamsRetriever {

    public abstract int size();

    protected static String ensureEnumString(final String input, final String[] enumStrings, 
            final boolean ignoreCase) throws JSONRPC2Error {
        for (String en : enumStrings) {
            if (ignoreCase) {
                if (en.toLowerCase().equals(input.toLowerCase())) {
                    return en;
                }
            } else {
                if (en.equals(input)) {
                    return en;
                }
            }
        }

        throw JSONRPC2Error.INVALID_PARAMS;
    }

    protected static <T extends Enum<T>> T ensureEnumString(final String input,
            final Class<T> enumClass, final boolean ignoreCase) throws JSONRPC2Error {
        for (T en : enumClass.getEnumConstants()) {
            if (ignoreCase) {
                if (en.toString().toLowerCase().equals(input.toLowerCase())) {
                    return en;
                }
            } else {
                if (en.toString().equals(input)) {
                    return en;
                }
            }
        }

        throw JSONRPC2Error.INVALID_PARAMS;
    }

}
