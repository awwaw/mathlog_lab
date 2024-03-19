package B.expression;

public class Implication extends DoubleArgumentExpression {

    public Implication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return left.toString() + "->" + right.toString();
    }
}
