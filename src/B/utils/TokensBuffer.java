package B.utils;

import java.util.List;

public class TokensBuffer {
    private int pos;
    private List<String> tokens;

    public TokensBuffer(List<String> tokens) {
        this.tokens = tokens;
        pos = 0;
    }

    public String next() {
        return tokens.get(pos++);
    }

    public void back() {
        pos--;
    }

    public int getPos() {
        return pos;
    }
}
