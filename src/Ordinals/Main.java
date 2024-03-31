package Ordinals;

import Ordinals.expression.Expression;
import Ordinals.expression.NormalizedExpression;
import Ordinals.parser.ExpressionBuilder;
import Ordinals.parser.ExpressionTokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
//    public static void main(String[] args) {
//        String expression = "(x^2)^(x^2)^(x^2)^x";
////        String expression = "x^x^x^x";
//        ExpressionTokenizer tokenizer = new ExpressionTokenizer(expression);
//        System.out.println(tokenizer.getTokens());
//        ExpressionBuilder builder = new ExpressionBuilder(expression);
//        Expression expr = builder.parse();
//        System.out.println(expr);
//    }

    private static void test() {
        try {
            List<String> lines = Files.readAllLines(Path.of("src/Ordinals/input.txt"));
            List<String> errors = new ArrayList<>();
            int passed = 0;
            for (String line : lines) {
                int eqIndex = line.indexOf("=");
                String left = line.substring(0, eqIndex);
                String right = line.substring(eqIndex + 1);
                Expression leftExpression = new ExpressionBuilder(left).parse();
                Expression rightExpression = new ExpressionBuilder(right).parse();
                NormalizedExpression leftNormalized = leftExpression.normalize();
                NormalizedExpression rightNormalized = rightExpression.normalize();
                if (!leftNormalized.equals(rightNormalized)) {
                    errors.add(
                            String.format(
                                    "Expected equality of: %n%s%nand%n%s",
                                    left,
                                    right
                            )
                    );
                } else {
                    passed++;
                }
            }
            System.out.println(String.format("Passed %d out of %d", passed, lines.size()));
            if (!errors.isEmpty()) {
                System.out.println("Here are all errors occurred during tests\n");
                for (String error : errors) {
                    System.out.println(error);
                    System.out.println("===================");
                }
            }
        } catch (IOException exc) {
            System.err.println("Couldn't open input file");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        int index = expression.indexOf("=");
        Expression parsedLeft = new ExpressionBuilder(expression.substring(0, index)).parse();
        Expression parsedRight = new ExpressionBuilder(expression.substring(index)).parse();
        NormalizedExpression normalizedLeft = parsedLeft.normalize();
        NormalizedExpression normalizedRight = parsedRight.normalize();
        if (normalizedLeft.equals(normalizedRight)) {
            System.out.println("Equal");
        } else {
            System.out.println("Not equal");
        }
    }
}
