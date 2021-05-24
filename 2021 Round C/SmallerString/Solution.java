// https://codingcompetitions.withgoogle.com/kickstart/round/0000000000435c44/00000000007ebe5e

import java.io.*;
import java.util.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    String[] input = br.readLine().split(" ");
    int N = Integer.parseInt(input[0]);
    int K = Integer.parseInt(input[1]);
    // read string
    String s = br.readLine();
    List<Integer> l = new ArrayList<>();
    for (char c : s.toCharArray()) {
      l.add((int) (c-'a'));
    }

    // main
    long MOD = (long) Math.pow(10, 9) + 7;
    long res = 0;

    // iterate half size of s
    int mid = (N-1)/2;
    for (int i=0; i<=mid; i++) {
      res = (res + l.get(i) * powerMod((long) K, (long) (mid-i), MOD)) % MOD;
    }

    // check if s itself is palindrom. If s is not a palindrom and create a
    // palindrom string using s's first half chars makes a smaller string than s
    // we add 1 to our result
    res += 1;
    for (int i=N/2; i<N; i++) {
      if (l.get(i) > l.get(N-i-1)) {
        break;
      }
      if ((l.get(i) < l.get(N-i-1)) || (i == N-1 && l.get(i) == l.get(N-i-1))) {
        res -= 1;
        break;
      }
    }
    res %= MOD;
    System.out.format("Case #%d: %d%n", trial, res);
    
  }

  public static long powerMod(long x, long y, long p) {
    long res = 1;
    x = x % p;

    if (x == 0) {
      return 0;
    }

    while (y > 0) {
      if ((y&1) != 0) {
        res = (res * x) % p;
      }
      y = y >> 1;
      x = (x*x)%p;
    }
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
