package Ordinals.parser;

import java.util.ArrayList;
import java.util.List;

public class ExpressionTokenizer {
    private final String expression;

    public ExpressionTokenizer(String expression) {
        this.expression = expression;
    }

    public List<String> getTokens() {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        StringBuilder currentVariable = new StringBuilder();
        while (i != expression.length()) {
            char currentChar = expression.charAt(i);
            if (Character.isWhitespace(currentChar) || currentChar == '\r' || currentChar == '\t') {
                i++;
                continue;
            }
            if (Character.isAlphabetic(currentChar) || Character.isDigit(currentChar) || currentChar == 0x0027) {
                if (currentVariable.isEmpty()) {
                    currentVariable = new StringBuilder(String.valueOf(currentChar));
                } else {
                    currentVariable.append(currentChar);
                }
            } else {
                if (!currentVariable.isEmpty()) {
                    tokens.add(currentVariable.toString());
                    currentVariable = new StringBuilder();
                }
                switch (currentChar) {
                    case '(', ')', '*', '+', '^' -> tokens.add(String.valueOf(currentChar));
                }
            }
            i++;
        }
        if (!currentVariable.isEmpty()) {
            tokens.add(currentVariable.toString());
        }
        tokens.add("$");
        return tokens;
    }
}

