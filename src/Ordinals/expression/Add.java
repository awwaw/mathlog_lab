package Ordinals.expression;

public class Add extends BinaryExpression {

    protected Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public NormalizedExpression normalize() {
        return left.normalize().add(right.normalize());
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "+" + right.toString() + ")";
    }
}
