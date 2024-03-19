package B.expression;

public abstract class DoubleArgumentExpression extends Expression {
    protected final Expression left;
    protected final Expression right;

    protected DoubleArgumentExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    protected Expression getLeft() {
        return left;
    }

    protected Expression getRight() {
        return right;
    }
}
