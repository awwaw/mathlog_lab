package Ordinals.expression;

import java.util.Comparator;

public class Term implements Comparable<Term> {
    private int value;
    private NormalizedExpression power = null;

    public Term(int value) {
        this.value = value;
    }

    public Term(int value, NormalizedExpression pow) {
        this.value = value;
        this.power = pow;
    }

    public Term clone() {
        return new Term(this.value, this.power);
    }

    public NormalizedExpression getPower() {
        return power == null ? new NormalizedExpression(0) : power;
    }

    public boolean hasPower() {
        return power != null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newValue) {
        value = newValue;
    }

    public void addValue(int newValue) {
        setValue(value + newValue);
    }

    public void multiplyValue(int newValue) {
        setValue(value * newValue);
    }

    @Override
    public int compareTo(Term o) {
        boolean poweredThis = power != null;
        boolean poweredThat = o.power != null;
        if (poweredThis != poweredThat) {
            return poweredThis ? 1 : -1;
        }
        if (power == null) {
            return Integer.compare(value, o.value);
        }

        Comparator<Term> cmp = Comparator.comparing(Term::getPower).thenComparing(Term::getValue);
        return cmp.compare(this, o);
    }
}