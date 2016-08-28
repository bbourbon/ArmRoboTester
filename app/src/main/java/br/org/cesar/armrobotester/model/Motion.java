package br.org.cesar.armrobotester.model;

/**
 * Created by bcb on 03/07/16.
 */
public class Motion {

    public static final int INVALID_VALUE = -1;
    public static final int MAX_POSITION_VALUE = 1024;
    public static final int MIN_POSITION_VALUE = 0;
    public static final int MAX_ROTATION_VALUE = 1024;
    public static final int MIN_ROTATION_VALUE = 0;
    private Type type;
    private int value;

    public Motion() {
        setType(Type.Invalid);
        setValue(INVALID_VALUE);
    }

    public Motion(Type t, int v) {
        setType(t);
        setValue(checkValue(t, v));
    }

    private int checkValue(Type t, int v) {
        int result;

        if (Type.Position == t && v >= MIN_POSITION_VALUE && v <= MAX_POSITION_VALUE) {
            result = v;
        } else if (Type.Rotation == t && v >= MIN_ROTATION_VALUE && v <= MAX_ROTATION_VALUE) {
            result = v;
        } else {
            result = INVALID_VALUE;
        }

        return result;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = checkValue(this.type, value);
    }

    @Override
    public String toString() {
        if (Type.Invalid == this.type) return "Invalid Motion";
        return "Motion[" + type.toString() + "]: " + value;
    }

    public enum Type {
        Position,
        Rotation,
        Invalid
    }
}
