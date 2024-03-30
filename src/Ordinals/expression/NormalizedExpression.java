package Ordinals.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NormalizedExpression implements Comparable<NormalizedExpression> {
    @Override
    public int compareTo(NormalizedExpression o) {
        int i = -1;
        int comparison = 0;
        int minSize = Math.min(size(), o.size());

        for (; i + 1 < minSize; i++) {
            comparison = get(i).compareTo(o.get(i));
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
        ordinal = new ArrayList<>();
        ordinal.addAll(other.getOrdinal());
    }

    public NormalizedExpression(List<Term> terms) {
        ordinal = new ArrayList<>();
        ordinal.addAll(terms);
    }

    private void addTerm(Term term) {
        ordinal.add(term);
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
        return ordinal.getLast().getPower().compareTo(other.getFirst().getPower());
    }

    public NormalizedExpression add(NormalizedExpression other) {
        if (this == other) {
            ordinal.getLast().setValue(ordinal.getLast().getValue() * 2);
            return this;
        }
        int comparison = getComparison(other);
        while (comparison < 0) {
            ordinal.removeLast();
            if (ordinal.isEmpty()) break;

            comparison = getComparison(other);
        }

        if (comparison == 0 && !ordinal.isEmpty()) {
            ordinal.getLast().addValue(other.getFirst().getValue());
            ordinal.addAll(other.getOrdinal().subList(1, other.ordinal.size()));
        } else {
            ordinal.addAll(other.getOrdinal());
        }
        return this;
    }

    public NormalizedExpression multiply(NormalizedExpression other) {
        final NormalizedExpression ZERO = NormalizedExpressionUtils.getZero();
        if (compareTo(ZERO) * other.compareTo(ZERO) == 0) {
            ordinal = ZERO.getOrdinal();
            return this;
        }

        NormalizedExpression totalResult = NormalizedExpressionUtils.getZero();
        for (int i = 0; i < other.getOrdinal().size(); i++) {
            NormalizedExpression localResult;
            Term rightDigit = other.get(i);
            if (rightDigit.getPower().compareTo(ZERO) == 0) {
                localResult = new NormalizedExpression(this);
                localResult.getFirst().multiplyValue(rightDigit.getValue());
            } else {
                NormalizedExpression power = new NormalizedExpression(ordinal.getFirst().getPower());
                power.add(other.get(i).getPower());
                List<Term> digits = List.of(
                        new Term(
                                other.getOrdinal()
                                        .get(i)
                                        .getValue(),
                                power
                        )
                );
                localResult = new NormalizedExpression(digits);
            }

            totalResult.add(localResult);
        }

        ordinal = totalResult.getOrdinal();
        return this;
    }

    public NormalizedExpression power(NormalizedExpression other) {
        NormalizedExpression w = new NormalizedExpression();
        NormalizedExpression result = new NormalizedExpression(1);

        if (NormalizedExpressionUtils.checkForZero(other)) { // x^0 = 1
            ordinal = result.getOrdinal();
            return this;
        }

        if (NormalizedExpressionUtils.checkForZero(this) || compareTo(result) == 0) { // 0^n = 0 && 1^n = 1
            return this;
        }

        if (compareTo(w) < 0 && other.compareTo(w) < 0) {
            ordinal.getFirst().setValue(
                    (int) Math.pow(
                            ordinal.getFirst().getValue(),
                            other.getFirst().getValue()
                    )
            );
            return this;
        }

        if (compareTo(w) < 0) {
            int leftFirstValue = ordinal.getFirst().getValue();
            int exp = 0;
            if (other.getLast().getPower() != null) {
                exp = other.getLast().getValue();
            }

            List<Term> newTerms = new ArrayList<>();
            for (int i = 0; i < other.size(); i++) {
                if (other.get(i).getPower() != null && other.get(i).getPower().compareTo(w) >= 0) {
                    newTerms.add(other.get(i).clone());
                } else if (other.get(i).getPower() != null && other.get(i).getPower().getFirst().getValue() != 1) {
                    newTerms.add(other.get(i).clone());
                    newTerms.get(newTerms.size() - 1).getPower().getFirst().addValue(-1);
                } else if (other.get(i).getPower() != null) {
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
                                        ordinal.getFirst().getPower()
                                )
                        )
                ).multiply(digit);
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
