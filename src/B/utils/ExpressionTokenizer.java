package B.utils;

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
            if (Character.isAlphabetic(currentChar)) {
                if (currentVariable.isEmpty()) {
                    currentVariable = new StringBuilder(String.valueOf(currentChar));
                } else {
                    currentVariable.append(currentChar);
                }
                i++;
            } else {
                if (!currentVariable.isEmpty()) {
                    tokens.add(currentVariable.toString());
                    currentVariable = new StringBuilder();
                }
                switch (currentChar) {
                    case '(', ')', '&', '!', ',' -> tokens.add(String.valueOf(currentChar));
                    case '-' -> {
                        tokens.add("->");
                        i++;
                    }
                    case '|' -> {
                        if (i + 1 < expression.length()) {
                            char next = expression.charAt(i + 1);
                            if (next == '-') {
                                tokens.add("|-");
                                i++;
                            } else {
                                tokens.add("|");
                            }
                        }
                    }
                }
                i++;
            }
        }
        if (!currentVariable.isEmpty()) {
            tokens.add(currentVariable.toString());
        }
        tokens.add("$");
        return tokens;
    }
}
