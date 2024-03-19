package B.expression;

public class And extends DoubleArgumentExpression {

    public And(Expression left, Expression right) {
        super(left, right);
    }

    public String toString() {
        return left.toString() + "&" + right.toString();
    }
}
