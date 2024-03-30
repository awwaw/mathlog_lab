package Ordinals.expression;

public interface Expression {
    NormalizedExpression normalize();

    @Override
    String toString();
}
