package B.proof;

import B.expression.*;

import java.util.*;

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

    public static boolean checkEquivalentLeft(Turnstile first, Turnstile second) {
        if (!(first.getLeft() instanceof Context && second.getLeft() instanceof Context)) {
            return false;
        }
        Context fst = (Context) first.getLeft();
        Context snd = (Context) second.getLeft();

        Map<String, Integer> exprsLeft = new HashMap<>();
        Map<String, Integer> exprsRight = new HashMap<>();

        for (Expression expr : fst.getExprs()) {
            String str = expr.toString();
            exprsLeft.put(str, exprsLeft.getOrDefault(str, 0) + 1);
        }

        for (Expression expr : snd.getExprs()) {
            String str = expr.toString();
            exprsRight.put(str, exprsRight.getOrDefault(str, 0) + 1);
        }
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

    @Override
    public String check(List<Expression> expressions, List<Integer> indexes) {
        throw new UnsupportedOperationException("wtf");
    }

    @Override
    public ProofType getType() {
        return ProofType.DEDUCTION;
    }
}
