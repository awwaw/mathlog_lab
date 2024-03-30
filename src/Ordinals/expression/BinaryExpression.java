package Ordinals.expression;

import java.util.Objects;

public abstract class BinaryExpression implements Expression {
    protected final Expression left;
    protected final Expression right;

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    protected final Expression getLeft() {
        return left;
    }

    protected final Expression getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }

        BinaryExpression expr = (BinaryExpression) o;
        return expr.getLeft().equals(getLeft()) &&
                expr.getRight().equals(getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, getClass());
    }
}
