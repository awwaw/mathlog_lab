package B.expression;

public class Or extends DoubleArgumentExpression {
    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "|" + right.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        Or expr = (Or) o;
        return expr.left.equals(left) && expr.right.equals(right);
    }

    @Override
    public Type getType() {
        return Type.OR;
    }
}
