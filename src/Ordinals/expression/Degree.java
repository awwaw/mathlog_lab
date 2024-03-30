package Ordinals.expression;

public class Degree extends BinaryExpression {
    protected Degree(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public NormalizedExpression normalize() {
        return left.normalize().power(right.normalize());
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "^" + right.toString() + ")";
    }
}
