package B.proof;

import B.expression.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AxiomeProof implements Proof {
    private final static List<Expression> AXIOMS = List.of(
            new Implication(
                    new Variable("A"),
                    new Implication(
                            new Variable("B"),
                            new Variable("A")
                    )
            ), // 1

            new Implication( // 2
                    new Implication(
                            new Variable("A"),
                            new Variable("B")
                    ),
                    new Implication(
                            new Implication(
                                    new Variable("A"),
                                    new Implication(
                                            new Variable("B"),
                                            new Variable("C")
                                    )
                            ),
                            new Implication(
                                    new Variable("A"),
                                    new Variable("C")
                            )
                    )
            ),

            new Implication( // 3
                    new Variable("A"),
                    new Implication(
                            new Variable("B"),
                            new And(
                                    new Variable("A"),
                                    new Variable("B")
                            )
                    )
            ),

            new Implication( // 4
                    new And(
                            new Variable("A"),
                            new Variable("B")
                    ),
                    new Variable("A")
            ),
            new Implication( // 5
                    new And(
                            new Variable("A"),
                            new Variable("B")
                    ),
                    new Variable("B")
            ),

            new Implication( // 6
                    new Variable("A"),
                    new Or(
                            new Variable("A"),
                            new Variable("B")
                    )
            ),

            new Implication( // 7
                    new Variable("B"),
                    new Or(
                            new Variable("A"),
                            new Variable("B")
                    )
            ),

            new Implication( // 8
                    new Implication(
                            new Variable("A"),
                            new Variable("C")
                    ),
                    new Implication(
                            new Implication(
                                    new Variable("B"),
                                    new Variable("C")
                            ),
                            new Implication(
                                    new Or(
                                            new Variable("A"),
                                            new Variable("B")
                                    ),
                                    new Variable("C")
                            )
                    )
            ),

            new Implication( // 9
                    new Implication(
                            new Variable("A"),
                            new Variable("B")
                    ),
                    new Implication(
                            new Implication(
                                    new Variable("A"),
                                    new Negation(new Variable("B"))
                            ),
                            new Negation(new Variable("A"))
                    )
            ),

            new Implication( // 10
                    new Negation(
                            new Negation(new Variable("A"))
                    ),
                    new Variable("A")
            )
    );

    private boolean check(Map<String, Expression> substitution, Expression axiom, Expression expr) {
        if (axiom instanceof DoubleArgumentExpression dax) {
            if (expr instanceof DoubleArgumentExpression dexpr) {
                if (expr instanceof Turnstile turn && turn.getLeft().toString().isEmpty()) {
                    return check(substitution, axiom, turn.getRight());
                }
                if (dexpr.getType() != dax.getType()) {
                    return false;
                }
                return check(substitution, dax.getLeft(), dexpr.getLeft()) &&
                        check(substitution, dax.getRight(), dexpr.getRight());
            } else {
                return false;
            }
        } else if (axiom instanceof Negation negAx) {
            if (expr instanceof  Negation negExpr) {
                return check(substitution, negAx.getExpr(), negExpr.getExpr());
            }
            return false;
        } else if (axiom instanceof Variable varAx) {
            String name = varAx.toString();
            if (substitution.containsKey(name)) {
                return substitution.get(name).equals(expr);
            } else {
                substitution.put(name, expr);
            }
        }
        return true;
    }

    @Override
    public String check(Expression expr) {
        if (expr instanceof Turnstile turn) {
            return check(turn.getRight());
        }
        for (int i = 0; i < AXIOMS.size(); i++) {
            Map<String, Expression> keys = new HashMap<>();
            Expression axiom = AXIOMS.get(i);
            if (check(keys, axiom, expr)) {
                return String.format("[Ax. sch. %d]", i + 1);
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
        return ProofType.AXIOME;
    }
}
