import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    int n = Integer.parseInt(br.readLine());
    String s = br.readLine();
    int[] left = new int[n]; // minimum distance to left bins
    int[] right = new int[n]; // minimum distance to right bins

    // left distance
    int dist = Integer.MAX_VALUE;
    for (int i=0; i<n; i++) {
      if (s.charAt(i) == '1') {
        dist = 0;
      } else if (dist < Integer.MAX_VALUE) {
        dist += 1;
      }
      left[i] = dist;
    }

    // right distance
    dist = Integer.MAX_VALUE;
    for (int i=n-1; i>=0; i--) {
      if (s.charAt(i) == '1') {
        dist = 0;
      } else if (dist < Integer.MAX_VALUE) {
        dist += 1;
      }
      right[i] = dist;
    }

    // get sum
    long sum = 0;
    for (int i=0; i<n; i++) {
      sum += Math.min(left[i], right[i]);
    }

    System.out.format("Case #%d: %d%n", trial, sum);
    
  }

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int test = Integer.parseInt(br.readLine());
    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
