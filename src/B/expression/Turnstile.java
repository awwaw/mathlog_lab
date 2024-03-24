package B.expression;

public class Turnstile extends DoubleArgumentExpression {

    public Turnstile(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public Type getType() {
        return Type.TURNSTILE;
    }

    @Override
    public String toString() {
        return left.toString() + "|-" + right.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        Turnstile turn = (Turnstile) o;
        return turn.left.equals(left) && turn.right.equals(right);
    }

    @Override
    public Expression clone() {
        return new Turnstile(left, right);
    }
}
