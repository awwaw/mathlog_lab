package B;

import B.expression.DoubleArgumentExpression;
import B.expression.Expression;
import B.expression.Turnstile;
import B.proof.*;
import B.utils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final Proof[] checks = new Proof[]{
                new AxiomeProof(),
                new HypothesesProof(),
                new DeductionProof(),
                new ModusPonensProof()
        };

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line = reader.readLine();;
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException ioException) {
            System.out.println("Sadly :( " + ioException.getMessage());
        }

        List<Expression> expressions = new ArrayList<>(
                lines.stream()
                        .map(expr -> new ExpressionBuilder(expr).parse())
                        .toList()
        );

        for (int i = 0; i < expressions.size(); i++) {
            Expression expression = expressions.get(i);
            boolean found = false;
            for (Proof check : checks) {
                String verdict = null;
                switch (check.getType()) {
                    case ProofType.AXIOME, ProofType.HYPOTHESIS -> {
                        verdict = check.check(expression);
                    }
                    case ProofType.DEDUCTION -> {
                        for (int j = i - 1; j >= 0; j--) {
                            verdict = check.check(expression.clone(), expressions.get(j).clone(), j + 1);
                            if (verdict != null) {
                                break;
                            }
                        }
                    }
                    case ProofType.MODUSPONENS -> {
                        if (expression instanceof Turnstile dae) {
                            List<Expression> candidates = new ArrayList<>();
                            List<Integer> indexes = new ArrayList<>();
                            candidates.add(expression);
                            indexes.add(0);
                            for (int j = i - 1; j >= 0; j--) {
                                Expression expr = expressions.get(j).clone();
                                if (expr instanceof Turnstile cand &&
                                        ((ModusPonensProof) check).checkEquivalent(cand, (Turnstile) dae.clone())) {
                                    candidates.add(expr);
                                    indexes.add(j);
                                }
                            }
                            verdict = check.check(candidates, indexes);
                        } /* else {
                            List<Expression> candidates = new ArrayList<>();
                            List<Integer> indexes = new ArrayList<>();
                            candidates.add(expression);
                            indexes.add(0);
                            for (int j = 0; j < expressions.size(); j++) {
                                if (i != j) {
                                    candidates.add(expressions.get(j));
                                    indexes.add(j);
                                }
                            }
                        } */
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + check.getClass());
                }

                if (verdict != null) {
                    System.out.printf("[%d] %s %s%n", i + 1, expression, verdict);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.printf("[%d] %s [Incorrect]%n", i + 1, expression);
            }
        }
    }
}
