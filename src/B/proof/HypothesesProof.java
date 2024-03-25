package B.proof;

import B.expression.Context;
import B.expression.Expression;
import B.expression.Turnstile;

import java.util.List;

public class HypothesesProof implements Proof {
    @Override
    public String check(Expression expr) {
        if (expr instanceof Turnstile turn) {
            if (turn.getLeft() instanceof Context leftContext) {
                int idx = leftContext.getExprs().indexOf(turn.getRight());
                if (idx != -1) {
                    return String.format("[Hyp. %d]", idx + 1);
                }
            } else { // In this case we have smth like (A&B) |- ...
                if (turn.getLeft().equals(turn.getRight())) {
                    return "[Hyp. 1]";
                }
            }
        }
        return null;
    }

    @Override
    public String check(Expression expr, Expression candidate, int index) {
        throw new UnsupportedOperationException("wtf");
    }

    @Override
    public String check(List<Expression> expressions, List<Integer> indexes) {
        throw new UnsupportedOperationException("wtf");
    }

    @Override
    public ProofType getType() {
        return ProofType.HYPOTHESIS;
    }
}
