package B.expression;

import java.lang.String;

public abstract class Expression implements Cloneable {
//    public abstract Expression getExpr();

    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract Expression clone();
}
