package Ordinals.parser;

import Ordinals.expression.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class ExpressionBuilder {
    private final String expressionString;

    public ExpressionBuilder(String expressionString) {
        this.expressionString = expressionString;

    }

    public Expression parse() {
        return parseLine(new TokensBuffer(new ExpressionTokenizer(expressionString).getTokens()));
    }

    private Expression parseLine(TokensBuffer buffer) {
        Expression context = parseAdd(buffer);
        String turnstile = buffer.next();
        if (turnstile.equals("$")) {
            return context;
        } else {
            throw new IllegalArgumentException("????");
        }
    }

    private Expression parseDegree(TokensBuffer buffer) {
        Expression left = parseOperand(buffer);
        List<Expression> res = new ArrayList<>();
        res.add(left);
        while (true) {
            String nextToken = buffer.next();
            if (nextToken.equals("^")) {
                res.add(parseOperand(buffer));
            } else {
                buffer.back();
                break;
            }
        }
        return concatList(res, Degree::new);
    }

    private Expression concatList(List<Expression> exprs, BiFunction<Expression, Expression, Expression> ctor) {
        int idx = exprs.size() - 2;
        Expression res = exprs.getLast();
        while (idx >= 0) {
            res = ctor.apply(exprs.get(idx--), res);
        }
        return res;
    }

    private Expression parseAdd(TokensBuffer buffer) {
        Expression left = parseMul(buffer);
        List<Expression> res = new ArrayList<>();
        res.add(left);
        while (true) {
            String nextToken = buffer.next();
            if (nextToken.equals("+")) {
                res.add(parseMul(buffer));
            } else {
                buffer.back();
                break;
            }
        }
        return concatList(res, Add::new);
    }

    private Expression parseMul(TokensBuffer buffer) {
        Expression left = parseDegree(buffer);
        List<Expression> res = new ArrayList<>();
        res.add(left);
        while (true) {
            String nextToken = buffer.next();
            if (nextToken.equals("*")) {
                res.add(parseDegree(buffer));
            } else {
                buffer.back();
                break;
            }
        }
        return concatList(res, Multiply::new);
    }

    private final Pattern pattern = Pattern.compile("-?\\d+");

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    private Expression parseOperand(TokensBuffer buffer) {
        String nextToken = buffer.next();
        if (nextToken.equals("(")) {
            Expression expr = parseAdd(buffer);
            String br = buffer.next();
            if (br.equals(")")) {
                return expr;
            } else {
                throw new RuntimeException("BRUH");
            }
        } else {
            if (isNumeric(nextToken)) {
                return new Const(Integer.parseInt(nextToken));
            }
            return new Variable(nextToken);
        }
    }
}
