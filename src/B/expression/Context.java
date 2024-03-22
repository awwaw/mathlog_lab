package B.expression;

import java.util.List;

public class Context extends Expression {

    private final List<Expression> exprs;

    public Context(List<Expression> exprs) {
        this.exprs = exprs;
    }

    @Override
    public String toString() {
        return String.join(",", exprs.stream().map(Expression::toString).toList());
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) {
            return false;
        }
        Context expr = (Context) o;
        if (expr.exprs.size() != exprs.size()) {
            return false;
        }

        for (int i = 0; i < exprs.size(); i++) {
            if (!exprs.get(i).equals(expr.exprs.get(i))) {
                return false;
            }
        }
        return true;
    }

    public List<Expression> getExprs() {
        return exprs;
    }
}
