import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // prefix sum and rolling window
    String[] s = br.readLine().split(" ");
    String L = String.valueOf(Long.parseLong(s[0]) - 1), R = s[1];

    long res = 0;
    int nl = L.length();
    int nr = R.length();

    // calculate number of boring numbers with digits between nl and nr
    for (int i=nl+1; i<nr; i++) {
      res += (long) Math.pow(5, i);
    }

    if (nl < nr) {
      // calculate number of boring numbers with digits nl but greater or equal than L
      res += (long) Math.pow(5, nl) - helper(L);
      // calculate number of boring numbers with digits nl but smaller or equal than R
      res += helper(R);
    } else {
      // nl == nr
      res += helper(R) - helper(L);
    }


    System.out.format("Case #%d: %d%n", trial, res);
    
  }

  public static long helper(String X) {
    // calculate number of boring numbers smaller than X with same number of digits as X
    int n = X.length();
    long res=0;
    for (int i=0; i<n; i++) {
      int d = X.charAt(i) - '0';
      if (((d ^ i) & 1) == 0) {
        // if d has same parity with i, then we can simply add 5^(n-i-1)
        res += (long) ((d+1)/2) * Math.pow(5, n-i-1);
        return res;
      } else {
        // if d is exactly boring digit, we can calculate number of boring numbers starting from d-2 for ith position
        res += (long) (d/2) * Math.pow(5, n-i-1);
      }
    }
    res += 1;
    return res;
  }
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int test = Integer.parseInt(br.readLine());

    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
