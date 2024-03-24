package B.expression;

public class And extends DoubleArgumentExpression {

    public And(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Type getType() {
        return Type.AND;
    }

    public String toString() {
        return "(" + left.toString() + "&" + right.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        And expr = (And) o;
        return expr.left.equals(left) && expr.right.equals(right);
    }

    @Override
    public Expression clone() {
        return new And(left, right);
    }
}
