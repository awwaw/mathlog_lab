package B.expression;

import java.util.Vector;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public Variable(Variable other) {
        this.name = other.name;
    }

    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        return ((Variable) o).name.equals(name);
    }

    @Override
    public Expression clone() {
        return new Variable(name);
    }
}