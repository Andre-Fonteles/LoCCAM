package br.ufc.great.syssu.base;

public class PatternField extends AbstractField {

    private String name;
    private Object value;
    private String type;
    private boolean valueWildCard;
    private static String[] wildCards =
        {"?", "?boolean", "?integer", "?float", "?string", "?array", "?object"};

    public PatternField(String name, Object value) {
    	verify(name, value);
        this.name = name;
        this.value = value;
        this.type = FieldUtils.getType(value);
        this.valueWildCard = containsWildCard(value);
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getType() {
        return type;
    }

    public boolean hasWildCardValue() {
        return valueWildCard;
    }

    private boolean containsWildCard(Object value) {
        for (String string : wildCards) {
            if(string.equals(value)) return true;
        }
        return false;
    }
    
}
