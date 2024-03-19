package B.expression;

public class Variable extends Expression {
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

//    @Override
    public Expression getExpr() {
        return this;
    }
}