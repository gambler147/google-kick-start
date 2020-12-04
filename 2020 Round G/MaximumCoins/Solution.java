import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    int N = Integer.parseInt(br.readLine());

    int[][] matrix = new int[N][N];

    // read 
    for (int i=0; i<N; i++) {
      String[] s = br.readLine().split(" ");
      for (int j=0; j<N; j++){
        matrix[i][j] = Integer.parseInt(s[j]);
      }
    }

    // find max, diagonal, i-j could range from -N+1, N-1
    int res = 0;
    for (int d=-N+1; d<=N-1; d++) {
      // find sum
      int tmp = 0;
      for (int i=Math.max(0, d); i<=Math.min(d+N-1, N-1); i++) {
        tmp += matrix[i][i-d];
      }
      res = Math.max(res, tmp);
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
