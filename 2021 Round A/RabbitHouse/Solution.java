import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // priority queue. Start from the highest cells to lowest
    String[] input = br.readLine().split(" ");
    int R = Integer.parseInt(input[0]);
    int C = Integer.parseInt(input[1]);
    
    int[][] matrix = new int[R][C];
    for (int i=0; i<R; i++) {
      String[] s = br.readLine().split(" ");
      for (int j=0; j<C; j++) {
        matrix[i][j] = Integer.parseInt(s[j]);
      }
    }

    // priority queue, elements with larger cell values will be put in the front of the queue
    PriorityQueue<List<Integer>> pq  = new PriorityQueue<List<Integer>>((l1,l2) -> l2.get(0) - l1.get(0));
    HashSet<List<Integer>> visited = new HashSet<>();
    
    // init
    for (int i=0; i<R; i++) {
      for (int j=0; j<C; j++) {
        pq.add(Arrays.asList(matrix[i][j], i, j));
      }
    }

    // compute result
    long res = 0;
    while (!pq.isEmpty()) {
      List<Integer> top = pq.poll();
      int i=top.get(1);
      int j=top.get(2);
      // System.out.format("%d, %d, %d%n", top.get(0), i,j);
      if (visited.contains(Arrays.asList(i,j))) {
        continue;
      }
      visited.add(Arrays.asList(i,j));
      // check its neighbors' cells
      for (int[] n : new int[][] {{i-1, j}, {i+1,j}, {i, j-1}, {i, j+1}}) {
        int x = n[0];
        int y = n[1];
        if (x >= 0 && x < R && y >= 0 && y < C) {
          if (matrix[x][y] >= matrix[i][j] - 1) {
            continue;
          } else {
            // add matrix[i][j] - 1 to matrix[x][y]
            // System.out.format("%d, %d, %d %d %n", x, y, matrix[x][y], matrix[i][j]);
            res += matrix[i][j]-1-matrix[x][y];
            matrix[x][y] = matrix[i][j]-1;
            pq.add(Arrays.asList(matrix[x][y], x, y));
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
