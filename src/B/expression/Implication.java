package B.expression;

public class Implication extends DoubleArgumentExpression {

    public Implication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Type getType() {
        return Type.IMPL;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "->" + right.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        Implication expr = (Implication) o;
        return expr.left.equals(left) && expr.right.equals(right);
    }

    @Override
    public Expression clone() {
        return new Implication(left, right);
    }
}
