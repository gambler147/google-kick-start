// https://codingcompetitions.withgoogle.com/kickstart/round/0000000000435c44/00000000007ec1cb

import java.io.*;
import java.util.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // this is basically finding positive integer solutions for n, and K
    // such that n^2 + (2K-1)*n = 2G
    // notice 2k-1, n > 0 so n^2 < 2G, so time complexity is approximately sqrt(G)
    long G = Long.parseLong(br.readLine());

    int res = 0; 
    for (long n=1; n <= (long) Math.floor(Math.sqrt(2*G)); n++) {
      long d = 2*G - n*n;
      if (d % n == 0 && d / n % 2 == 1) {
        res += 1;
      }
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
