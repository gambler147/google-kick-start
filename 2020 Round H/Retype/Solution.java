import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // prefix sum and rolling window
    String[] s = br.readLine().split(" ");
    int N, K, S;
    N = Integer.parseInt(s[0]);
    K = Integer.parseInt(s[1]);
    S = Integer.parseInt(s[2]);

    int res = K-1 + Math.min(1+N, (K-S) + (N-S)+1);

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
