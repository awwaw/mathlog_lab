package B.expression;

public abstract class DoubleArgumentExpression extends Expression {
    protected final Expression left;
    protected final Expression right;

    protected DoubleArgumentExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public abstract Type getType();
}
