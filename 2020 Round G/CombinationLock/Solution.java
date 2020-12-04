import java.io.*;
import java.util.Arrays;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // prefix sum and rolling window
    String[] s = br.readLine().split(" ");
    int W = Integer.parseInt(s[0]);
    int N = Integer.parseInt(s[1]);

    // read wheels value
    int[] arr = new int[2*W];
    s = br.readLine().split(" ");
    for (int i=0; i<W; i++) {
      arr[i] = Integer.parseInt(s[i]);
    }

    // append arr[i] + N to the array to handle wrap around cases
    for (int i=0; i<W; i++) {
      arr[i+W] = arr[i] + N;
    }

    Arrays.sort(arr);
    // calculate prefix sum
    long[] prefix = new long[2*W];
    prefix[0] = arr[0];
    for (int i=1; i<2*W; i++) {
      prefix[i] = prefix[i-1] + arr[i];
    }
    
    long res = Long.MAX_VALUE;
    // rolling window
    for (int l=0; l<W; l++) {
      int r = l + W - 1;
      int m = (l+r) >> 1;
      long curr = prefix[m];  // cumulative sum from arr[l] to arr[m]
      if (l>0) curr -= prefix[l-1];
      // calculate distance from arr(l, m) to arr[m]
      curr = (long) (m-l+1) * arr[m] - curr;
      // calculate distance from arr[m] to arr(m, r)
      curr += prefix[r] - prefix[m] - (long) (r-m) * arr[m];
      System.out.println(curr);
      res = Math.min(res, curr);
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
