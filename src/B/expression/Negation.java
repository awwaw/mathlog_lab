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

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        return expr.equals(((Negation) o).expr);
    }
}
