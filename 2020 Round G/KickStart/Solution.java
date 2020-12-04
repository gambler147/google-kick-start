import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    String s = br.readLine();

    int kick = 0; // prefix sum of number of kicks
    int res = 0;

    for (int i=0; i<s.length(); i++) {
      if (s.charAt(i) == 'K') {
        // check if previous chars are 'KIC'
        if (i >= 3 && s.charAt(i-1) == 'C' && s.charAt(i-2) == 'I' && s.charAt(i-3) == 'K') {
          kick++;
        }
      } else if (s.charAt(i) == 'T') {
        // check if we encounter a "START"
        if (i>=4 && s.charAt(i-1) == 'R' && s.charAt(i-2) == 'A' && s.charAt(i-3) == 'T' && s.charAt(i-4) == 'S') {
          res += kick;
        }
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