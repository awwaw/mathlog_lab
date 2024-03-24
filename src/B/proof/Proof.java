package B.proof;

import B.expression.Expression;

import java.util.List;

public interface Proof {
    String check(Expression expr);

    String check(Expression expr, Expression candidate, int index);

    String check(List<Expression> expressions, List<Integer> indexes);

    ProofType getType();
}
