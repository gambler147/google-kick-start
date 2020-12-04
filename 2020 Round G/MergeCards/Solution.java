import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // prefix sum and rolling window
    int N = Integer.parseInt(br.readLine());
    
    // read values
    String[] s = br.readLine().split(" ");
    int[] arr = new int[N];
    for (int i=0; i<N; i++) {
      arr[i] = Integer.parseInt(s[i]);
    }

    // dp, dp[i][j] means expected number of times it has been added for ith element in total j elements (i<=j)

    double[][] dp = new double[N][N];

    for (int j=1; j<N; j++) {
      for (int i=0; i<=j; i++) {
        // probability that ith element is picked
        if (i == 0) {
          dp[i][j] = 1.0/j + dp[i][j-1];
        } else if (i == j) {
          dp[i][j] = 1.0/j + dp[i-1][j-1];
        } else {
          dp[i][j] = 1.0/j * (1 + dp[i-1][j-1]) + 1.0/j * (1 + dp[i][j-1]) + 1.0*(i-1)/j * dp[i-1][j-1] + 1.0*(j-i-1)/j * dp[i][j-1];
        }
      }
    }

    // calculate final results
    double res = 0;
    for (int i=0; i<N; i++) {
      res += dp[i][N-1] * arr[i];
    }
    System.out.format("Case #%d: %f%n", trial, res);
    
  }
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int test = Integer.parseInt(br.readLine());

    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
