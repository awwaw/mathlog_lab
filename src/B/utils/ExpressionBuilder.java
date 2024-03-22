package B.utils;

import B.expression.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.nio.charset.Charset;
import java.util.*;
import java.lang.*;
import java.util.function.BiFunction;

public class ExpressionBuilder {
    private final String expressionString;
    private TokensBuffer buffer;

    public ExpressionBuilder(String expressionString) {
        this.expressionString = expressionString;

    }

//    public Expression buildExpression() {
////        List<String> tokens = new ExpressionTokenizer(expressionString).getTokens();
////        int turnstileIndex = tokens.indexOf("|-");
////        List<String> left = tokens.subList(0, turnstileIndex);
////        List<String> right = tokens.subList(turnstileIndex, tokens.size());
////        return new Turnstile(parse(new TokensBuffer(left)), parse(new TokensBuffer(right)));
//    }

    public Expression parse() {
        return parseLine(new TokensBuffer(new ExpressionTokenizer(expressionString).getTokens()));
    }

    private Expression parseContext(TokensBuffer buffer) {
        if (buffer.next().equals("|-")) {
            buffer.back();
            return new Context(List.of());
        }
        buffer.back();
        List<Expression> exprs = new ArrayList<>();
        exprs.add(parseImpl(buffer));
        while (true) {
            String nextToken = buffer.next();
            if (nextToken.equals(",")) {
                exprs.add(parseImpl(buffer));
            } else {
                buffer.back();
                if (exprs.size() == 1) {
                    return exprs.getFirst();
                }
                return new Context(exprs);
            }
        }
    }

    private Expression parseLine(TokensBuffer buffer) {
        Expression context = parseContext(buffer);
        String turnstile = buffer.next();
        if (turnstile.equals("$")) {
            return context;
        }
        if (!turnstile.equals("|-")) {
            throw new IllegalArgumentException("pupupu");
        }
        Expression expr = parseImpl(buffer);
        return new Turnstile(context, expr);
    }

    private Expression parseImpl(TokensBuffer buffer) {
        Expression left = parseOr(buffer);
        String nextToken = buffer.next();
        if (nextToken.equals("->")) {
            Expression right = parseImpl(buffer);
            return new Implication(left, right);
        } else {
            buffer.back();
            return left;
        }
    }

    private Expression concatList(List<Expression> exprs, BiFunction<Expression, Expression, Expression> ctor) {
        int idx = 1;
        Expression res = exprs.getFirst();
        while (idx < exprs.size()) {
            res = ctor.apply(res, exprs.get(idx++));
        }
        return res;
    }

    private Expression parseOr(TokensBuffer buffer) {
        Expression left = parseAnd(buffer);
        List<Expression> res = new ArrayList<>();
        res.add(left);
        while (true) {
            String nextToken = buffer.next();
            if (nextToken.equals("|")) {
                res.add(parseAnd(buffer));
            } else {
                buffer.back();
                break;
            }
        }
        return concatList(res, Or::new);
    }

    private Expression parseAnd(TokensBuffer buffer) {
        Expression left = parseNot(buffer);
        List<Expression> res = new ArrayList<>();
        res.add(left);
        while (true) {
            String nextToken = buffer.next();
            if (nextToken.equals("&")) {
                res.add(parseNot(buffer));
            } else {
                buffer.back();
                break;
            }
        }
        return concatList(res, And::new);
    }

    private Expression parseNot(TokensBuffer buffer) {
        String nextToken = buffer.next();
        if (nextToken.equals("(")) {
            Expression expr = parseImpl(buffer);
            String br = buffer.next();
            if (br.equals(")")) {
                return expr;
            } else {
                throw new RuntimeException("BRUH");
            }
        } else if (nextToken.equals("!")) {
            return new Negation(parseNot(buffer));
        } else {
            buffer.back();
            return parseVariable(buffer);
        }
    }

    private Expression parseVariable(TokensBuffer buffer) {
        String nextToken = buffer.next();
        return new Variable(nextToken);
    }
}