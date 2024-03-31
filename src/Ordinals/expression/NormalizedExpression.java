package Ordinals.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NormalizedExpression implements Comparable<NormalizedExpression> {
    @Override
    public int compareTo(NormalizedExpression o) {
        int i = -1;
        int comparison = 0;
        int minSize = Math.min(size(), o.size());

        for (; i + 1 < minSize; i++) {
            comparison = get(i + 1).compareTo(o.get(i + 1));
            if (comparison != 0) {
                break;
            }
        }
        i++;
        if (i == size() && i < o.size()) {
            return -1;
        }
        if (i == o.size() && i < size()) {
            return 1;
        }
        return comparison;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }

        NormalizedExpression expr = (NormalizedExpression) o;
        return compareTo(expr) == 0;
    }

    private List<Term> ordinal;

    public List<Term> getOrdinal() {
        return ordinal;
    }

    public NormalizedExpression() {
        ordinal = new ArrayList<>(Collections.singleton(new Term(1, new NormalizedExpression(1))));
    }

    public NormalizedExpression(int value) {
        ordinal = new ArrayList<>(Collections.singleton(new Term(value)));
    }

    public NormalizedExpression(NormalizedExpression other) {
        ordinal = other.ordinal.stream()
                .map(Term::clone)
                .collect(Collectors.toList());
    }

    public NormalizedExpression(List<Term> terms) {
        ordinal = terms.stream()
                .map(Term::clone)
                .collect(Collectors.toList());
    }

    private int size() {
        return ordinal.size();
    }

    private Term getFirst() {
        return ordinal.get(0);
    }

    private Term getLast() {
        return ordinal.get(size() - 1);
    }

    private Term get(int idx) {
        return ordinal.get(idx);
    }

    /*
        Arithmetic operations
     */

    private int getComparison(NormalizedExpression other) {
        return getLast().getPower().compareTo(other.getFirst().getPower());
    }

    public NormalizedExpression add(NormalizedExpression other) {
        if (this == other) {
            getLast().setValue(getLast().getValue() * 2);
            return this;
        }
        int comparison = getComparison(other);
        while (comparison < 0) {
            ordinal.remove(size() - 1);
            if (ordinal.isEmpty()) {
                break;
            }

            comparison = getComparison(other);
        }

        if (comparison == 0 && !ordinal.isEmpty()) {
            getLast().setValue(getLast().getValue() + other.getFirst().getValue());
            ordinal.addAll(NormalizedExpressionUtils.getSublist(other, 1, other.size()));
        } else {
            ordinal.addAll(NormalizedExpressionUtils.getSublist(other, 0, other.size()));
        }
        return this;
    }

    public NormalizedExpression multiply(NormalizedExpression other) {
        final NormalizedExpression ZERO = NormalizedExpressionUtils.getZero();
        if (NormalizedExpressionUtils.checkForZero(this) || NormalizedExpressionUtils.checkForZero(other)) {
            ordinal = ZERO.getOrdinal();
            return this;
        }

        NormalizedExpression totalResult = NormalizedExpressionUtils.getZero();
        for (int i = 0; i < other.size(); i++) {
            NormalizedExpression localResult;
            Term rightDigit = other.get(i).clone();
            if (NormalizedExpressionUtils.checkForZero(rightDigit.getPower())) {
                localResult = new NormalizedExpression(this);
                localResult.getFirst().value *= rightDigit.getValue();
            } else {
                NormalizedExpression power = new NormalizedExpression(getFirst().getPower());
                power.add(other.get(i).getPower());
                List<Term> digits = List.of(
                        new Term(
                                rightDigit.getValue(),
                                power
                        )
                );
                localResult = new NormalizedExpression(digits);
            }

            if (i == 0) {
                totalResult.ordinal = new ArrayList<>(localResult.getOrdinal());
            } else {
                totalResult.add(localResult);
            }
        }

        ordinal = totalResult.getOrdinal();
        return this;
    }

    public NormalizedExpression power(NormalizedExpression other) {
        NormalizedExpression w = new NormalizedExpression();
        NormalizedExpression result = NormalizedExpressionUtils.getOne();

        if (NormalizedExpressionUtils.checkForZero(other)) { // x^0 = 1
            ordinal = result.getOrdinal();
            return this;
        }

        if (NormalizedExpressionUtils.checkForZero(this)
                || compareTo(NormalizedExpressionUtils.getOne()) == 0) { // 0^n = 0 && 1^n = 1
            return this;
        }

        if (compareTo(w) < 0 && other.compareTo(w) < 0) {
            getFirst().setValue(
                    (int) Math.pow(
                            getFirst().getValue(),
                            other.getFirst().getValue()
                    )
            );
            return this;
        }

        if (compareTo(w) < 0) {
            int leftFirstValue = getFirst().getValue();
            int exp = 0;
            if (!other.getLast().hasPower()) {
                exp = other.getLast().getValue();
            }

            List<Term> newTerms = new ArrayList<>();
            for (int i = 0; i < other.size(); i++) {
                if (other.get(i).hasPower() && other.get(i).getPower().compareTo(w) >= 0) {
                    newTerms.add(other.get(i).clone());
                } else if (other.get(i).hasPower() && other.get(i).getPower().getFirst().getValue() != 1) {
                    newTerms.add(other.get(i).clone());
                    newTerms.get(newTerms.size() - 1).getPower().getFirst().addValue(-1);
                } else if (other.get(i).hasPower()) {
                    newTerms.add(new Term(other.get(i).getValue()));
                }
            }

            NormalizedExpression wp = new NormalizedExpression(newTerms);
            ordinal = new ArrayList<>(
                    Collections.singleton(
                            new Term(
                                    (int) Math.pow(leftFirstValue, exp),
                                    wp
                            )
                    )
            );
            return this;
        }

        for (int i = 0; i < other.size(); i++) {
            NormalizedExpression digit = NormalizedExpressionUtils.createDigit(other.get(i));
            NormalizedExpression localResult;
            if (digit.compareTo(w) >= 0) {
                localResult = NormalizedExpressionUtils.createDigit(
                        new Term(
                                1,
                                new NormalizedExpression(
                                        getFirst().getPower()
                                ).multiply(digit)
                        )
                );
            } else {
                localResult = this.power(
                        digit.getFirst().getValue()
                );
            }

            if (i == 0) {
                result.ordinal = new ArrayList<>(localResult.getOrdinal());
            } else {
                result.multiply(localResult);
            }
        }
        ordinal = result.ordinal;
        return this;
    }

    public NormalizedExpression power(int pow) {
        if (pow == 0) {
            return NormalizedExpressionUtils.getOne();
        }
        NormalizedExpression res = new NormalizedExpression(this);
        if (pow % 2 == 0) {
            return res.multiply(res).power(pow / 2);
        }
        return res.multiply(this.power(pow - 1));
    }
}