package B.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

public class FastScanner implements AutoCloseable {

    private BufferedReader reader;
    private StringTokenizer tokenizer;

    public FastScanner(File file) {
        try {
            reader = Files.newBufferedReader(Path.of(file.toURI()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FastScanner() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String next() {
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                tokenizer = new StringTokenizer(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return tokenizer.nextToken();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
