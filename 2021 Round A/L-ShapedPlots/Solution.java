import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    String[] input = br.readLine().split(" ");
    int R = Integer.parseInt(input[0]);
    int C = Integer.parseInt(input[1]);

    // read matrix
    String[] s;
    int[][] matrix = new int[R][C];
    for (int i=0; i<R; i++) {
      s = br.readLine().split(" ");
      for (int j=0; j<C; j++) {
        matrix[i][j] = Integer.parseInt(s[j]);
      }
    }

    // prefix sum array for four directions
    int[][][] prefix = new int[R][C][4];
    // top and left prefix
    for (int i=0; i<R; i++) {
      for (int j=0; j<C; j++) {
        if (matrix[i][j] == 1) {
          prefix[i][j][0] = 1;
          prefix[i][j][1] = 1;
          if (i > 0) {
            prefix[i][j][0] += prefix[i-1][j][0];
          }
          if (j > 0) {
            prefix[i][j][1] += prefix[i][j-1][1];
          }
        }
      }
    }

    // bottom and right prefix
    for (int i=R-1; i>=0; i--) {
      for (int j=C-1; j>=0; j--) {
        if (matrix[i][j] == 1) {
          prefix[i][j][2] = 1;
          prefix[i][j][3] = 1;
          if (i < R-1) {
            prefix[i][j][2] += prefix[i+1][j][2];
          }
          if (j < C-1) {
            prefix[i][j][3] += prefix[i][j+1][3];
          }
        }
      }
    }

    // count number of L-shaped plots
    int res = 0;
    for (int i=0; i<R; i++) {
      for (int j=0; j<C; j++) {
        int l = prefix[i][j][0];
        int t = prefix[i][j][1];
        int r = prefix[i][j][2];
        int b = prefix[i][j][3];
        
        // four possible combinations
        // if two segments have length m,n
        // then the number of valid L-shaped plots are min(m-1, 1+(n-4)/2) + min(n-1, 1+(m-4)/2)
        for (int[] x : new int[][] {{l,t}, {l,b}, {r,t}, {r,b}}) {
          if (x[0] >= 2 && x[1] >= 4) {
            res += Math.min(x[0]-1, 1+(x[1]-4)/2);
          }
          if (x[1] >= 2 && x[0] >= 4) {
            res += Math.min(x[1]-1, 1+(x[0]-4)/2);
          }
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
