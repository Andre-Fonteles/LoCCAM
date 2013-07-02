package br.ufc.great.syssu.jsonrpc2;

public final class JSONRPC2ParamsType {

    private final int intValue;
    private final String name;
    public static final int NO_PARAMS_CONST = 0;
    public static final JSONRPC2ParamsType NO_PARAMS =
            new JSONRPC2ParamsType("NO_PARAMS", NO_PARAMS_CONST);
    public static final int ARRAY_CONST = 1;
    public static final JSONRPC2ParamsType ARRAY =
            new JSONRPC2ParamsType("ARRAY", ARRAY_CONST);
    public static final int OBJECT_CONST = 2;
    public static final JSONRPC2ParamsType OBJECT =
            new JSONRPC2ParamsType("OBJECT", OBJECT_CONST);

    private JSONRPC2ParamsType(final String name, final int intValue) {
        this.name = name;
        this.intValue = intValue;
    }

    public String getName() {
        return name;
    }

    public int intValue() {
        return intValue;
    }

    public static JSONRPC2ParamsType valueOf(final int intValue) {
        switch (intValue) {
            case 0:
                return NO_PARAMS;
            case 1:
                return ARRAY;
            case 2:
                return OBJECT;
            default:
                return null;
        }
    }

    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (o == this) {
            return true;
        } else if (o instanceof JSONRPC2ParamsType) {
            return (intValue == ((JSONRPC2ParamsType) o).intValue);
        } else {
            return false;
        }
    }

    public String toString() {
        return name;
    }
}
