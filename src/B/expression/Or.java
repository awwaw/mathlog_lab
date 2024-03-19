package B.expression;

public class Or extends DoubleArgumentExpression {
    protected Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return left.toString() + "|" + right.toString();
    }
}
