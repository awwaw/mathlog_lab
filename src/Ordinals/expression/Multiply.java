package Ordinals.expression;

public class Multiply extends BinaryExpression {
    public Multiply(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public NormalizedExpression normalize() {
        return left.normalize().multiply(right.normalize());
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }
}
