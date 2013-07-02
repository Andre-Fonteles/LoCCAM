package br.ufc.great.syssu.jsonrpc2.util;

import java.util.List;
import java.util.Map;

import br.ufc.great.syssu.jsonrpc2.JSONRPC2Error;

public class PositionalParamsRetriever
        extends ParamsRetriever {

    private List params = null;

    public PositionalParamsRetriever(final List params) {
        this.params = params;
    }

    public int size() {
        return params.size();
    }

    public boolean hasParameter(final int position) {
        if (position >= params.size()) {
            return false;
        } else {
            return true;
        }
    }

    public void ensureParameter(final int position)
            throws JSONRPC2Error {
        if (position >= params.size()) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public void ensureParameter(final int position, final Class clazz)
            throws JSONRPC2Error {
        ensureParameter(position, clazz, false);
    }

    public void ensureParameter(final int position, final Class clazz, final boolean allowNull)
            throws JSONRPC2Error {
        ensureParameter(position);

        Object value = params.get(position);

        if (value == null) {
            if (allowNull) {
                return;
            } else {
                throw JSONRPC2Error.INVALID_PARAMS;
            }
        }

        if (!clazz.isAssignableFrom(value.getClass())) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public Object get(final int position)
            throws JSONRPC2Error {
        ensureParameter(position);
        return params.get(position);
    }

    public Object get(final int position, final Class clazz)
            throws JSONRPC2Error {
        return get(position, clazz, false);
    }

    public Object get(final int position, final Class clazz, final boolean allowNull)
            throws JSONRPC2Error {
        ensureParameter(position, clazz, allowNull);
        return params.get(position);
    }

    public Object getOpt(final int position, final Class clazz, final Object defaultValue)
            throws JSONRPC2Error {
        return getOpt(position, clazz, false, defaultValue);
    }

    public Object getOpt(final int position, final Class clazz, final boolean allowNull, final Object defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(position)) {
            return defaultValue;
        }
        ensureParameter(position, clazz, allowNull);
        return params.get(position);
    }

    public String getString(final int position)
            throws JSONRPC2Error {
        return getString(position, false);
    }

    public String getString(final int position, final boolean allowNull)
            throws JSONRPC2Error {
        return (String) get(position, String.class, allowNull);
    }

    public String getOptString(final int position, final String defaultValue)
            throws JSONRPC2Error {
        return getOptString(position, false, defaultValue);
    }

    public String getOptString(final int position, final boolean allowNull, final String defaultValue)
            throws JSONRPC2Error {
        return (String) getOpt(position, String.class, allowNull, defaultValue);
    }

    public String getEnumString(final int position, final String[] enumStrings)
            throws JSONRPC2Error {
        return getEnumString(position, enumStrings, false);
    }

    public String getEnumString(final int position, final String[] enumStrings, final boolean ignoreCase)
            throws JSONRPC2Error {
        String value = (String) get(position, String.class);
        return ensureEnumString(value, enumStrings, ignoreCase);
    }

    public String getOptEnumString(final int position, final String[] enumStrings, final String defaultValue)
            throws JSONRPC2Error {
        return getOptEnumString(position, enumStrings, defaultValue, false);
    }

    public String getOptEnumString(final int position, final String[] enumStrings, final String defaultValue, final boolean ignoreCase)
            throws JSONRPC2Error {
        String value = (String) getOpt(position, String.class, defaultValue);
        return ensureEnumString(value, enumStrings, ignoreCase);
    }

    public <T extends Enum<T>> T getEnum(final int position, final Class<T> enumClass)
            throws JSONRPC2Error {
        return getEnum(position, enumClass, false);
    }

    public <T extends Enum<T>> T getEnum(final int position, final Class<T> enumClass, final boolean ignoreCase)
            throws JSONRPC2Error {
        String value = (String) get(position, String.class);
        return ensureEnumString(value, enumClass, ignoreCase);
    }

    public <T extends Enum<T>> T getOptEnum(final int position, final Class<T> enumClass, final String defaultValue)
            throws JSONRPC2Error {
        return getOptEnum(position, enumClass, defaultValue, false);
    }

    public <T extends Enum<T>> T getOptEnum(final int position, final Class<T> enumClass, final String defaultValue, final boolean ignoreCase)
            throws JSONRPC2Error {
        String value = (String) getOpt(position, String.class, defaultValue);
        return ensureEnumString(value, enumClass, ignoreCase);
    }

    public boolean getBoolean(final int position)
            throws JSONRPC2Error {
        return (Boolean) get(position, Boolean.class);
    }

    public boolean getOptBoolean(final int position, final boolean defaultValue)
            throws JSONRPC2Error {
        return (Boolean) getOpt(position, Boolean.class, defaultValue);
    }

    public int getInt(final int position)
            throws JSONRPC2Error {
        Number number = (Number) get(position, Long.class);
        return number.intValue();
    }

    public int getOptInt(final int position, final int defaultValue)
            throws JSONRPC2Error {
        Number number = (Number) getOpt(position, Long.class, new Long(defaultValue));
        return number.intValue();
    }

    public long getLong(final int position)
            throws JSONRPC2Error {
        Number number = (Number) get(position, Long.class);
        return number.longValue();
    }

    public long getOptLong(final int position, final long defaultValue)
            throws JSONRPC2Error {
        Number number = (Number) getOpt(position, Long.class, defaultValue);
        return number.longValue();
    }

    public float getFloat(final int position)
            throws JSONRPC2Error {
        Number number = (Number) get(position, Double.class);
        return number.floatValue();
    }

    public float getOptFloat(final int position, final float defaultValue)
            throws JSONRPC2Error {
        Number number = (Number) getOpt(position, Double.class, new Double(defaultValue));
        return number.floatValue();
    }

    public double getDouble(final int position)
            throws JSONRPC2Error {
        Number number = (Number) get(position, Double.class);
        return number.doubleValue();
    }

    public double getOptDouble(final int position, final double defaultValue)
            throws JSONRPC2Error {
        Number number = (Number) getOpt(position, Double.class, defaultValue);
        return number.doubleValue();
    }

    public List getList(final int position)
            throws JSONRPC2Error {
        return getList(position, false);
    }

    public List getList(final int position, final boolean allowNull)
            throws JSONRPC2Error {
        return (List) get(position, List.class, allowNull);
    }

    public List getOptList(final int position, final List defaultValue)
            throws JSONRPC2Error {
        return getOptList(position, false, defaultValue);
    }

    public List getOptList(final int position, final boolean allowNull, final List defaultValue)
            throws JSONRPC2Error {
        return (List) getOpt(position, List.class, allowNull, defaultValue);
    }

    public String[] getStringArray(final int position)
            throws JSONRPC2Error {
        return getStringArray(position, false);
    }

    public String[] getStringArray(final int position, final boolean allowNull)
            throws JSONRPC2Error {
        try {
            Object value = get(position);

            if (value == null) {
                if (allowNull) {
                    return null;
                } else {
                    throw JSONRPC2Error.INVALID_PARAMS;
                }
            }

            List list = (List) value;
            return (String[]) list.toArray(new String[]{});

        } catch (ClassCastException e) {
            throw JSONRPC2Error.INVALID_PARAMS;
        } catch (ArrayStoreException e) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public String[] getOptStringArray(final int position, final String[] defaultValue)
            throws JSONRPC2Error {
        return getOptStringArray(position, false, defaultValue);
    }

    public String[] getOptStringArray(final int position, final boolean allowNull, final String[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(position)) {
            return defaultValue;        }

        return getStringArray(position, allowNull);
    }

    public boolean[] getBooleanArray(final int position)
            throws JSONRPC2Error {
        try {
            List list = getList(position);
            boolean[] booleanArray = new boolean[list.size()];

            for (int i = 0; i < list.size(); i++) {
                booleanArray[i] = (Boolean) list.get(i);
            }

            return booleanArray;
        } catch (ClassCastException e) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public boolean[] getOptBooleanArray(final int position, final boolean[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(position)) {
            return defaultValue;
        }
        return getBooleanArray(position);
    }

    public int[] getIntArray(final int position)
            throws JSONRPC2Error {
        try {
            List list = getList(position);
            int[] intArray = new int[list.size()];

            for (int i = 0; i < list.size(); i++) {
                Number number = (Number) list.get(i);
                intArray[i] = number.intValue();
            }
            return intArray;
        } catch (ClassCastException e) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public int[] getOptIntArray(final int position, final int[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(position)) {
            return defaultValue;
        }
        return getIntArray(position);
    }

    public long[] getLongArray(final int position)
            throws JSONRPC2Error {
        try {
            List list = getList(position);
            long[] longArray = new long[list.size()];

            for (int i = 0; i < list.size(); i++) {
                Number number = (Number) list.get(i);
                longArray[i] = number.longValue();
            }
            return longArray;
        } catch (ClassCastException e) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public long[] getOptLongArray(final int position, final long[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(position)) {
            return defaultValue;
        }
        return getLongArray(position);
    }

    public float[] getFloatArray(final int position)
            throws JSONRPC2Error {
        try {
            List list = getList(position);
            float[] floatArray = new float[list.size()];

            for (int i = 0; i < list.size(); i++) {
                Number number = (Number) list.get(i);
                floatArray[i] = number.floatValue();
            }
            return floatArray;
        } catch (ClassCastException e) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public float[] getOptFloatArray(final int position, final float[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(position)) {
            return defaultValue;
        }
        return getFloatArray(position);
    }

    public double[] getDoubleArray(final int position)
            throws JSONRPC2Error {
        try {
            List list = getList(position);
            double[] doubleArray = new double[list.size()];

            for (int i = 0; i < list.size(); i++) {
                Number number = (Number) list.get(i);
                doubleArray[i] = number.doubleValue();
            }
            return doubleArray;
        } catch (ClassCastException e) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public double[] getOptDoubleArray(final int position, final double[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(position)) {
            return defaultValue;
        }
        return getDoubleArray(position);
    }

    public Map getMap(final int position)
            throws JSONRPC2Error {
        return getMap(position, false);
    }

    public Map getMap(final int position, final boolean allowNull)
            throws JSONRPC2Error {
        return (Map) get(position, Map.class, allowNull);
    }

    public Map getOptMap(final int position, final Map defaultValue)
            throws JSONRPC2Error {
        return getOptMap(position, false, defaultValue);
    }

    public Map getOptMap(final int position, final boolean allowNull, final Map defaultValue)
            throws JSONRPC2Error {
        return (Map) getOpt(position, Map.class, allowNull, defaultValue);
    }
    
}
