import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    String[] input = br.readLine().split(" ");
    String s = br.readLine();
    int N = Integer.parseInt(input[0]);
    int K = Integer.parseInt(input[1]);
    // just count number of mismatched i si != s(N-i+1)
    int mismatch = 0;
    for (int i=0; i<N/2; i++) {
      if (s.charAt(i) != s.charAt(N-i-1)) {
        mismatch++;
      }
    }
    System.out.format("Case #%d: %d%n", trial, Math.abs(K-mismatch));

  }

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int test = Integer.parseInt(br.readLine());

    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
