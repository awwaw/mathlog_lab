package Ordinals.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NormalizedExpressionUtils {
    public static boolean checkForZero(NormalizedExpression expr) {
        return expr.compareTo(getZero()) == 0;
    }

    public static NormalizedExpression getZero() {
        return new NormalizedExpression(0);
    }

    public static NormalizedExpression getOne() {
        return new NormalizedExpression(1);
    }

    public static NormalizedExpression createDigit(Term term) {
        return new NormalizedExpression(new ArrayList<>(Collections.singleton(term)));
    }

    public static List<Term> getSublist(NormalizedExpression other, int start, int end) {
        return other.getOrdinal()
                .subList(start, end)
                .stream()
                .map(Term::clone)
                .collect(Collectors.toList());
    }
}
