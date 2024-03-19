package B.expression;

import java.util.Objects;

public class Negation extends Expression {
    private final Expression expr;

    public Negation(Expression expr) {
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public String toString() {
        return "!" + expr.toString();
    }
}
