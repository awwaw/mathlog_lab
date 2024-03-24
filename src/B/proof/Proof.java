package B.proof;

import B.expression.Expression;

public interface Proof {
    String check(Expression expr);

    String check(Expression expr, Expression candidate, int index);
}
