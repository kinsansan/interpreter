/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newlang8;

/**
 *
 * @author Admin
 */
public class ValueImpl implements Value {

    String s;
    int i;
    double d;
    boolean b;
    ValueType type;

    public ValueImpl(String svalue) {
        s = svalue;
        type = ValueType.STRING;
    }

    public ValueImpl(int ivalue) {
        i = ivalue;
        type = ValueType.INTEGER;
    }

    public ValueImpl(double value) {
        d = value;
        type = ValueType.DOUBLE;
    }

    public ValueImpl(boolean value) {
        b = value;
        type = ValueType.BOOL;
    }

    @Override
    public String getSValue() {
        if (type == ValueType.INTEGER) {
            return String.valueOf(i);
        } else if (type == ValueType.DOUBLE) {
            return String.valueOf(d);
        } else if (type == ValueType.BOOL) {
            return String.valueOf(b);
        }

        return s;
    }

    @Override
    public int getIValue() {
        return i;
    }

    @Override
    public double getDValue() {
        return d;
    }

    @Override
    public boolean getBValue() {
        return b;
    }

    @Override
    public ValueType getType() {
        return type;
    }

    @Override
    public String toString() {
        return getSValue(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
