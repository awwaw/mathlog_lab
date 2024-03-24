package B.expression;

import java.util.ArrayList;
import java.util.List;

public class Context extends Expression {

    private ArrayList<Expression> exprs;

    public Context(ArrayList<Expression> exprs) {
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

    @Override
    public Expression clone() {
        ArrayList<Expression> expressions = new ArrayList<>(exprs);
        return new Context(expressions);
    }

    public List<Expression> getExprs() {
        return exprs;
    }

    public void addExpression(Expression expr) {
        exprs.add(expr);
    }
}
