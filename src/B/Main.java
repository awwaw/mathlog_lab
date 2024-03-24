package B;

import B.expression.Expression;
import B.proof.AxiomeProof;
import B.proof.DeductionProof;
import B.proof.HypothesesProof;
import B.proof.Proof;
import B.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private final static List<String> AXIOMES = List.of(
            "A->B->A",
            "(A->B)->(A->B->C)->(A->C)",
            "A->B->A&B",
            "A&B->A",
            "A&B->B",
            "A->A|B",
            "B->A|B",
            "(A->C)->(B->C)->(A|B->C)",
            "(A->B)->(A->!B)->!A",
            "!!A->A"
    );

    private final static Map<String, String> SUBSTITUTIONS = Map.of(
            "A", "(P&Q)",
            "B", "(W|Z)",
            "C", "(!(S->T))"
    );

    private static void test() {
        for (int i = 0; i < AXIOMES.size(); i++) {
            String scheme = AXIOMES.get(i);
            String ans = String.format("[Ax. sch. %d]", i + 1);
            System.out.println("\nWas - " + scheme);
            for (String variable : SUBSTITUTIONS.keySet()) {
                scheme = scheme.replaceAll(variable, SUBSTITUTIONS.get(variable));
            }
            System.out.println("Now - " + scheme + "\n");
            Expression expression = new ExpressionBuilder(scheme).parse();
            System.out.println("Expression - " + expression);
            AxiomeProof axProof = new AxiomeProof();
            String res = axProof.check(expression);
            System.out.printf("Correct - %s\nActual - %s%n", ans, res);
        }
    }

    public static void main(String[] args) {
//        test();

        final Proof[] checks = new Proof[]{
                new AxiomeProof(),
                new HypothesesProof(),
                new DeductionProof()
        };
//        Scanner scanner = new Scanner(System.in);
//        String line = scanner.next();
//        Expression expression = new ExpressionBuilder(line).parse();
//        for (Proof check : checks) {
//            String verdict = check.check(expression);
//            if (verdict != null) {
//                System.out.println(verdict);
//            }
//        }

        List<String> lines = List.of(
//                "|-(A&B)->A",
//                "|-(A&B)->B",
//                "A&B|-A",
//                "A&B|-B",
//                "A->B,C,D,D|-D"
                "A,B,C|-D",
                "A,B|-C->D",
                "A|-B->C->D"
        );

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
                try {
                    verdict = check.check(expression);
                } catch (UnsupportedOperationException uoe) {
                    for (int j = i - 1; j >= 0; j--) {
                        verdict = check.check(expression.clone(), expressions.get(j).clone(), j + 1);
                        if (verdict != null) {
                            break;
                        }
                    }
                }
                if (verdict != null) {
                    System.out.printf("[%d] %s %s%n", i + 1, expression, verdict);
                    found = true;
                }
            }
            if (!found) {
                System.out.printf("[%d] %s [Incorrect]%n", i + 1, expression);
            }
        }
    }
}
