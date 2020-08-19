package com.github.rafaelsilvestri.filterdsl.tree;

/**
 * Operand holds the type and its value.
 *
 * @author Rafael Silvestri
 */
public class Operand {
    private OperandType type;
    private Object value;

    public Operand(OperandType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public OperandType getType() {
        return type;
    }

    public void setType(OperandType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
