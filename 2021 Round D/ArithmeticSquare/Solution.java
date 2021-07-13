// https://codingcompetitions.withgoogle.com/kickstart/round/0000000000435c44/00000000007ec1cb

import java.io.*;
import java.util.*;

public class Solution {
  public static void solve(int trial, InputReader in, OutputWriter out) throws IOException {
    // this is basically finding positive integer solutions for n, and K
    // such that n^2 + (2K-1)*n = 2G
    // notice 2k-1, n > 0 so n^2 < 2G, so time complexity is approximately sqrt(G)
    long[][] G = new long[3][3];

    for (int i=0; i<3; i++) {
      // read data
      if (i!=1) {
        for (int j=0; j<3; j++) {
          G[i][j] = (long) in.nextInt();
        }
      } else {
        G[i][0] = (long) in.nextInt();
        G[i][2] = (long) in.nextInt();
      }
    }

    int res = 0;
    // calculate 4 base rows and cols
    for (int i: new int[] {0, 2}) {
      if (G[i][0] + G[i][2] == 2*G[i][1]) {
        res += 1;
      }

      if (G[0][i] + G[2][i] == 2*G[1][i]) {
        res += 1;
      }
    }

    // count end-point sums of mid row and col and two diagonals
    HashMap<Long, Integer> sums = new HashMap<>();
    long[] vals = new long[] {G[1][0] + G[1][2], G[0][1]+G[2][1], G[0][0]+G[2][2], G[2][0] + G[0][2]};
    for (long val : vals) {
      sums.put(val, sums.getOrDefault(val, 0)+1);
    }
    
    // check max count
    int cnt=0;
    for (long key : sums.keySet()) {
      if (key%2 == 0) {
        cnt = Math.max(cnt, sums.get(key));
      }
    }

    System.out.format("Case #%d: %d%n", trial, res+cnt);
  }

  static class InputReader {
    private InputStream stream;
    private byte[] buf = new byte[1024];
    private int curChar;
    private int numChars;
    private InputReader.SpaceCharFilter filter;

    public InputReader(InputStream stream) {
        this.stream = stream;
    }

    public int read() {
        if (numChars == -1) {
            throw new InputMismatchException();
        }
        if (curChar >= numChars) {
            curChar = 0;
            try {
                numChars = stream.read(buf);
            } catch (IOException e) {
                throw new InputMismatchException();
            }
            if (numChars <= 0) {
                return -1;
            }
        }
        return buf[curChar++];
    }

    public int nextInt() {
        int c = read();
        while (isSpaceChar(c)) c = read();
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        int res = 0;
        do {
            if (c < '0' || c > '9')
                throw new InputMismatchException();
            res *= 10;
            res += c - '0';
            c = read();
        } while (!isSpaceChar(c));
        return res * sgn;
    }

    public boolean isSpaceChar(int c) {
        if (filter != null) {
            return filter.isSpaceChar(c);
        }
        return isWhitespace(c);
    }

    public static boolean isWhitespace(int c) {
        return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
    }

    public interface SpaceCharFilter {
        public boolean isSpaceChar(int ch);

    }

  }

  static class OutputWriter {
    private final PrintWriter writer;

    public OutputWriter(OutputStream outputStream) {
        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
    }

    public OutputWriter(Writer writer) {
        this.writer = new PrintWriter(writer);
    }

    public void flush() {
        writer.flush();
    }

    public void println(Object... objects) {
        for (int i = 0; i < objects.length; i++) {
            if (i != 0) {
                writer.print(' ');
            }
            writer.print(objects[i]);
        }
        writer.print('\n');
    }

    public void close() {
        writer.close();
    }

  }
  public static void main(String[] args) throws IOException {
    InputStream inputStream = System.in;
    OutputStream outputStream = System.out;
    InputReader in = new InputReader(inputStream);
    OutputWriter out = new OutputWriter(outputStream);
    int test = in.nextInt();
    for (int trial=1; trial <= test; trial++) {
      solve(trial, in, out);
    }
  }
}
