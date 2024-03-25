package B.proof;

import B.expression.*;

import java.util.ArrayList;
import java.util.List;

public class ModusPonensProof implements Proof {
    @Override
    public String check(Expression expr) {
        return null;
    }

    @Override
    public String check(Expression expr, Expression candidate, int index) {
        return null;
    }

    public boolean checkEquivalent(Turnstile first, Turnstile second) {
        Context ctx1 = new Context(new ArrayList<>());
        Context ctx2 = new Context(new ArrayList<>());
        if (!(first.getLeft() instanceof Context)) {
            ctx1.addExpression(first.getLeft());
        } else {
            ctx1 = (Context) first.getLeft();
        }
        if (!(second.getLeft() instanceof Context)) {
            ctx2.addExpression(first.getLeft());
        } else {
            ctx2 = (Context) second.getLeft();
        }
        return DeductionProof.checkEquivalentLeft(new Turnstile(ctx1, null), new Turnstile(ctx2, null));
    }

    /*
    List<Expression> exprs && exprs.getFirst() == expr we are going to check -> String
     */
    @Override
    public String check(List<Expression> expressions, List<Integer> indexes) {
        Expression example;
        if (expressions.getFirst() instanceof DoubleArgumentExpression dae) {
            example = dae.getRight();
        } else {
            example = expressions.getFirst();
        }

        for (int i = 1; i < expressions.size(); i++) {
            Expression expr = expressions.get(i);
            if (expr instanceof DoubleArgumentExpression dexpr) {
                if (dexpr.getRight() instanceof Implication impl && example.equals(impl.getRight())) {
                    Expression need = impl.getLeft();
                    for (int j = 1; j < expressions.size(); j++) {
                        Expression e = expressions.get(j);
                        if (e instanceof DoubleArgumentExpression de) {
                            if (de.getRight().equals(need)) {
                                return String.format("[M.P. %d, %d]", indexes.get(j) + 1, indexes.get(i) + 1);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ProofType getType() {
        return ProofType.MODUSPONENS;
    }
}
