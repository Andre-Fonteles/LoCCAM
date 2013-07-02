package br.ufc.great.syssu.jsonrpc2.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufc.great.syssu.jsonrpc2.JSONRPC2Error;

public class NamedParamsRetriever extends ParamsRetriever {

    private Map<String, ?> params = null;

    public NamedParamsRetriever(final Map params) {
        this.params = params;
    }

    public int size() {
        return params.size();
    }

    public boolean hasParameter(final String name) {
        if (params.containsKey(name)) {
            return true;
        } else {
            return false;
        }
    }

    public String[] getNames() {
        Set keyset = params.keySet();
        return (String[]) keyset.toArray(new String[]{});
    }

    public void ensureParameters(String[] mandatoryNames) throws JSONRPC2Error {
        ensureParameters(mandatoryNames, null);
    }

    public void ensureParameters(String[] mandatoryNames, String[] optionalNames) throws JSONRPC2Error {
        Map paramsCopy = (Map) ((HashMap) params).clone();

        for (String name : mandatoryNames) {
            if (paramsCopy.containsKey(name)) {
                paramsCopy.remove(name);
            } else {
                throw JSONRPC2Error.INVALID_PARAMS;
            }
        }

        if (optionalNames != null) {
            for (String name : optionalNames) {
                if (paramsCopy.containsKey(name)) {
                    paramsCopy.remove(name);
                }
            }
        }

        int remainingKeys = paramsCopy.size();

        if (remainingKeys > 0) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public void ensureParameter(final String name)
            throws JSONRPC2Error {
        if (name == null) {
            throw new IllegalArgumentException();
        }

        if (!params.containsKey(name)) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public void ensureParameter(final String name, final Class clazz) throws JSONRPC2Error {
        ensureParameter(name, clazz, false);
    }

    public void ensureParameter(final String name, final Class clazz, final boolean allowNull)
            throws JSONRPC2Error {
        ensureParameter(name);

        Object value = params.get(name);

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

    public Object get(final String name) throws JSONRPC2Error {
        ensureParameter(name);
        return params.get(name);
    }

    public Object get(final String name, final Class clazz) throws JSONRPC2Error {
        return get(name, clazz, false);
    }

    public Object get(final String name, final Class clazz, final boolean allowNull)
            throws JSONRPC2Error {
        ensureParameter(name, clazz, allowNull);
        return params.get(name);
    }

    public Object getOpt(final String name, final Class clazz, final Object defaultValue)
            throws JSONRPC2Error {
        return getOpt(name, clazz, false, defaultValue);
    }

    public Object getOpt(final String name, final Class clazz, final boolean allowNull,
            final Object defaultValue) throws JSONRPC2Error {
        if (!hasParameter(name)) {
            return defaultValue;
        }
        ensureParameter(name, clazz, allowNull);
        return params.get(name);
    }

    public String getString(final String name) throws JSONRPC2Error {
        return getString(name, false);
    }

    public String getString(final String name, final boolean allowNull) throws JSONRPC2Error {
        return (String) get(name, String.class, allowNull);
    }

    public String getOptString(final String name, final String defaultValue) throws JSONRPC2Error {
        return getOptString(name, false, defaultValue);
    }

    public String getOptString(final String name, final boolean allowNull, final String defaultValue)
            throws JSONRPC2Error {
        return (String) getOpt(name, String.class, allowNull, defaultValue);
    }

    public String getEnumString(final String name, final String[] enumStrings) throws JSONRPC2Error {
        return getEnumString(name, enumStrings, false);
    }

    public String getEnumString(final String name, final String[] enumStrings,
            final boolean ignoreCase) throws JSONRPC2Error {
        String value = (String) get(name, String.class);
        return ensureEnumString(value, enumStrings, ignoreCase);
    }

    public String getOptEnumString(final String name, final String[] enumStrings,
            final String defaultValue) throws JSONRPC2Error {
        return getOptEnumString(name, enumStrings, defaultValue, false);
    }

    public String getOptEnumString(final String name, final String[] enumStrings,
            final String defaultValue, final boolean ignoreCase) throws JSONRPC2Error {
        String value = (String) getOpt(name, String.class, defaultValue);
        return ensureEnumString(value, enumStrings, ignoreCase);
    }

    public <T extends Enum<T>> T getEnum(final String name, final Class<T> enumClass)
            throws JSONRPC2Error {
        return getEnum(name, enumClass, false);
    }

    public <T extends Enum<T>> T getEnum(final String name, final Class<T> enumClass,
            final boolean ignoreCase) throws JSONRPC2Error {
        String value = (String) get(name, String.class);
        return ensureEnumString(value, enumClass, ignoreCase);
    }

    public <T extends Enum<T>> T getOptEnum(final String name, final Class<T> enumClass,
            final T defaultValue) throws JSONRPC2Error {
        return getOptEnum(name, enumClass, defaultValue, false);
    }

    public <T extends Enum<T>> T getOptEnum(final String name, final Class<T> enumClass,
            final T defaultValue, final boolean ignoreCase) throws JSONRPC2Error {
        String value = (String) getOpt(name, String.class, defaultValue.toString());
        return ensureEnumString(value, enumClass, ignoreCase);
    }

    public boolean getBoolean(final String name)
            throws JSONRPC2Error {
        return (Boolean) get(name, Boolean.class);
    }

    public boolean getOptBoolean(final String name, final boolean defaultValue)
            throws JSONRPC2Error {
        return (Boolean) getOpt(name, Boolean.class, defaultValue);
    }

    public int getInt(final String name)
            throws JSONRPC2Error {
        Number number = (Number) get(name, Long.class);
        return number.intValue();
    }

    public int getOptInt(final String name, final int defaultValue)
            throws JSONRPC2Error {
        Number number = (Number) getOpt(name, Long.class, new Long(defaultValue));
        return number.intValue();
    }

    public long getLong(final String name)
            throws JSONRPC2Error {
        Number number = (Number) get(name, Long.class);
        return number.longValue();
    }

    public long getOptLong(final String name, final long defaultValue)
            throws JSONRPC2Error {
        Number number = (Number) getOpt(name, Long.class, defaultValue);
        return number.longValue();
    }

    public float getFloat(final String name)
            throws JSONRPC2Error {
        Number number = (Number) get(name, Double.class);
        return number.floatValue();
    }

    public float getOptFloat(final String name, final float defaultValue)
            throws JSONRPC2Error {
        Number number = (Number) getOpt(name, Double.class, new Double(defaultValue));
        return number.floatValue();
    }

    public double getDouble(final String name)
            throws JSONRPC2Error {
        Number number = (Number) get(name, Double.class);
        return number.doubleValue();
    }

    public double getOptDouble(final String name, final double defaultValue)
            throws JSONRPC2Error {
        Number number = (Number) getOpt(name, Double.class, defaultValue);
        return number.doubleValue();
    }

    public List getList(final String name)
            throws JSONRPC2Error {
        return getList(name, false);
    }

    public List getList(final String name, final boolean allowNull)
            throws JSONRPC2Error {
        return (List) get(name, List.class, allowNull);
    }

    public List getOptList(final String name, final List defaultValue)
            throws JSONRPC2Error {
        return getOptList(name, false, defaultValue);
    }

    public List getOptList(final String name, final boolean allowNull, final List defaultValue)
            throws JSONRPC2Error {
        return (List) getOpt(name, List.class, allowNull, defaultValue);
    }

    public String[] getStringArray(final String name)
            throws JSONRPC2Error {
        return getStringArray(name, false);
    }

    public String[] getStringArray(final String name, final boolean allowNull)
            throws JSONRPC2Error {
        try {
            Object value = get(name);

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

    public String[] getOptStringArray(final String name, final String[] defaultValue)
            throws JSONRPC2Error {
        return getOptStringArray(name, false, defaultValue);
    }

    public String[] getOptStringArray(final String name, final boolean allowNull,
            final String[] defaultValue) throws JSONRPC2Error {
        if (!hasParameter(name)) {
            return defaultValue;
        }
        return getStringArray(name, allowNull);
    }

    public boolean[] getBooleanArray(final String name)
            throws JSONRPC2Error {
        try {
            List list = getList(name);
            boolean[] booleanArray = new boolean[list.size()];

            for (int i = 0; i < list.size(); i++) {
                booleanArray[i] = (Boolean) list.get(i);
            }

            return booleanArray;

        } catch (ClassCastException e) {
            throw JSONRPC2Error.INVALID_PARAMS;
        }
    }

    public boolean[] getOptBooleanArray(final String name, final boolean[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(name)) {
            return defaultValue;
        }
        return getBooleanArray(name);
    }

    public int[] getIntArray(final String name)
            throws JSONRPC2Error {
        try {
            List list = getList(name);
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

    public int[] getOptIntArray(final String name, final int[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(name)) {
            return defaultValue;
        }
        return getIntArray(name);
    }

    public long[] getLongArray(final String name)
            throws JSONRPC2Error {

        try {
            List list = getList(name);
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

    public long[] getOptLongArray(final String name, final long[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(name)) {
            return defaultValue;
        }
        return getLongArray(name);
    }

    public float[] getFloatArray(final String name)
            throws JSONRPC2Error {
        try {
            List list = getList(name);
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

    public float[] getOptFloatArray(final String name, final float[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(name)) {
            return defaultValue;
        }
        return getFloatArray(name);
    }

    public double[] getDoubleArray(final String name)
            throws JSONRPC2Error {
        try {
            List list = getList(name);
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

    public double[] getOptDoubleArray(final String name, final double[] defaultValue)
            throws JSONRPC2Error {
        if (!hasParameter(name)) {
            return defaultValue;
        }
        return getDoubleArray(name);
    }

    public Map getMap(final String name)
            throws JSONRPC2Error {
        return getMap(name, false);
    }

    public Map getMap(final String name, final boolean allowNull)
            throws JSONRPC2Error {
        return (Map) get(name, Map.class, allowNull);
    }

    public Map getOptMap(final String name, final Map defaultValue)
            throws JSONRPC2Error {
        return getOptMap(name, false, defaultValue);
    }

    public Map getOptMap(final String name, final boolean allowNull, final Map defaultValue)
            throws JSONRPC2Error {
        return (Map) getOpt(name, Map.class, allowNull, defaultValue);
    }

}
