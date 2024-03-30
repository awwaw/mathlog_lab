package Ordinals.expression;

import java.util.Objects;

public class Const extends ExpressionArgument {

    private final int value;

    public Const(int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    @Override
    public NormalizedExpression normalize() {
        return new NormalizedExpression(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }

        Const other = (Const) o;
        return other.getValue() == value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
