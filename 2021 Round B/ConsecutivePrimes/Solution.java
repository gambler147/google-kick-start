import java.io.*;
import java.math.BigInteger;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    long Z = Long.parseLong(br.readLine());

    // this is the ceil of the first prime number
    long ceil = (long) Math.sqrt(Z);
    // now we need to find a prime number <= ceil
    // then we build a prime list whose value is less than sqrt(ceil)
    // so that we can check if a number is a prime or not
    int factor_ceil = (int) Math.sqrt(ceil)+100;
    boolean[] sieve = new boolean[factor_ceil+1];
    for (int i=2; i<=factor_ceil; i++) {
      sieve[i] = true;
    }

    for (int p=2; p*p <= factor_ceil; p++) {
      if (sieve[p] == true) {
        for (int i=p*p; i<=factor_ceil; i+=p) {
          sieve[i] = false;
        }
      }
    }

    // then we start from ceil, find closest prime number
    long p1=ceil;
    while (p1 >= 2) {
      if (p1!= 2 && p1!=3 && p1%6 != 1 && p1%6 != 5) {
        p1--;
        continue;
      }
      if (isPrime(p1, sieve)) {
        break;
      } else {
        p1--;
      }
    }

    // find a prime number that is greater than p1 and a prime number that
    // is smaller than p1
    long p2u=p1+1;
    long p2d=p1-1;

    while (p2u >= 2) {
      if (p2u != 3 && p2u !=2 && p2u%6 != 1 && p2u%6 != 5) {
        p2u++;
        continue;
      }
      if (isPrime(p2u, sieve)) {
        break;
      } else {
        p2u++;
      }
    }
    
    if (p2d >= 2) {
      while (true) {
        if (p2d != 3 && p2d != 2 && p2d%6 != 1 && p2d%6 != 5) {
          p2d--;
          continue;
        }
        if (isPrime(p2d, sieve)) {
          break;
        } else {
          p2d--;
        }
      }
    }
    long res;
    BigInteger tmp = new BigInteger(String.valueOf(p1)).multiply(
      new BigInteger(String.valueOf(p2u)));

    if (tmp.compareTo(new BigInteger(String.valueOf(Z))) > 0) {
      res = p1 * p2d;
    } else {
      res = p1 * p2u;
    }

    System.out.format("Case #%d: %d%n", trial, res);
    
  }

  public static boolean isPrime(long v, boolean[] sieve) {
    int n = sieve.length;
    if (v == 2 || v == 3) {
      return true;
    }
    for (int i=2; i<n && i<v; i++) {
      if (sieve[i]==true && v%i == 0) {
        return false;
      }
    }
    return true;
  }
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int test = Integer.parseInt(br.readLine());
    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
