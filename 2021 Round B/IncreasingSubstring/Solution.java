import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    int n = Integer.parseInt(br.readLine());
    String s = br.readLine();
    // dp
    int[] dp = new int[n];
    dp[0]=1;
    for (int i=1; i<n; i++) {
      if (s.charAt(i) > s.charAt(i-1)) {
        dp[i] = dp[i-1]+1;
      } else {
        dp[i]=1;
      }
    }

    StringBuilder sb = new StringBuilder();
    for (int i=0; i<n; i++) {
      sb.append(dp[i]);
      sb.append(" ");
    }

    System.out.format("Case #%d: %s%n", trial, sb.toString());
    
  }

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int test = Integer.parseInt(br.readLine());
    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
