package B.expression;

public abstract class DoubleArgumentExpression extends Expression {
    protected final Expression left;
    protected final Expression right;

    protected DoubleArgumentExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public final Expression getLeft() {
        return left;
    }

    public final Expression getRight() {
        return right;
    }

    public Type getType() {
        return null;
    }
}
