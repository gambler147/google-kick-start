import java.io.*;
import java.util.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    int n = Integer.parseInt(br.readLine());

    int[] X = new int[n];
    int[] Y = new int[n];
    long res = 0;
    // get positions
    for (int i=0; i<n; i++) {
      String[] s = br.readLine().split(" ");
      X[i] = Integer.parseInt(s[0]);
      Y[i] = Integer.parseInt(s[1]);
    }

    // the final vertical line should be the median of Y
    Arrays.sort(Y);
    int median = Y[n/2];
    for (int i=0; i<n; i++) {
      res += Math.abs(Y[i] - median);
    }

    // the final vertical line should be the median of X+i, where X is sorted
    // since we are trying to find a value t such that minimized |X1 - t| + |X2 - (t+1)| + ... |Xn - (t+n)|
    Arrays.sort(X);
    for (int i=0; i<n; i++) {
      X[i] -= i;
    }
    Arrays.sort(X);
    median = X[n/2];
    for (int i=0; i<n; i++) {
      res += Math.abs(X[i] - median);
    }

    System.out.format("Case #%d: %d%n", trial, res);
    
  }
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int test = Integer.parseInt(br.readLine());

    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
