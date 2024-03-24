package B.proof;

import B.expression.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeductionProof implements Proof {

    private Turnstile preprocess(final Expression expr) {
        if (expr instanceof Turnstile turn) {
            Context hypos = new Context(new ArrayList<>());
            if (turn.getLeft() instanceof Context h) {
                hypos = (Context) h.clone();
            } else {
                ArrayList<Expression> hy = new ArrayList<>();
                hy.add(turn.getLeft().clone());
                hypos = new Context(hy);
            }

            Expression right = turn.getRight().clone();
            while (right instanceof Implication impl) {
                hypos.addExpression(impl.getLeft());
                right = impl.getRight().clone();
            }
            return new Turnstile(hypos, right);
        } else {
            return null;
        }
    }

    @Override
    public String check(Expression expr) {
        throw new UnsupportedOperationException("wtf");
    }

    private boolean checkEquivalentLeft(Turnstile first, Turnstile second) {
        if (!(first.getLeft() instanceof Context && second.getLeft() instanceof Context)) {
            return false;
        }
        Context fst = (Context) first.getLeft();
        Context snd = (Context) second.getLeft();

        Set<String> exprsLeft = new HashSet<>(fst.getExprs().stream().map(Expression::toString).toList());
        Set<String> exprsRight = new HashSet<>(snd.getExprs().stream().map(Expression::toString).toList());

        return exprsLeft.equals(exprsRight);
    }

    @Override
    public String check(final Expression expr, final Expression candidate, int index) {
        Turnstile processedLeft = preprocess(expr.clone());
        Turnstile processedRight = preprocess(candidate.clone());
        if (checkEquivalentLeft(processedLeft, processedRight) &&
                processedLeft.getRight().equals(processedRight.getRight())) {
            return String.format("[Ded. %d]", index);
        }
        return null;
    }
}
