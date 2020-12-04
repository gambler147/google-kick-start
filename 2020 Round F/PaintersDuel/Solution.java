import java.io.*;
import java.util.*;

public class PaintersDuel {
  public static void solve(int trial, BufferedReader br) throws IOException {
    String[] input = br.readLine().split(" ");
    int S = Integer.parseInt(input[0]);
    int RA = Integer.parseInt(input[1]);
    int PA = Integer.parseInt(input[2]);
    int RB = Integer.parseInt(input[3]);
    int PB = Integer.parseInt(input[4]);
    int C = Integer.parseInt(input[5]);

    // using an 2-d array to represent availabel rooms
    boolean[][] avail = new boolean[S+1][2*S];
    // initialization
    for (int i=1; i<=S; i++) {
      for (int j=1; j<2*i; j++) {
        avail[i][j] = true;
      }
    }
    avail[RA][PA] = false;
    avail[RB][PB] = false;

    // rooms under construction
    for (int c=1; c<=C; c++) {
      input = br.readLine().split(" ");
      int i=Integer.parseInt(input[0]);
      int j= Integer.parseInt(input[1]);
      avail[i][j] = false;
    }
    
    int res = dfs(RA, PA, RB, PB, S, 0, avail, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
    System.out.format("Case #%d: %d%n", trial, res);
  }

  public static int dfs(int ra, int pa, int rb, int pb, int S, int score, boolean[][] avail, boolean Alma, int alpha, int beta) {
    // return maximum score alma can get, where pa, ra, pb, rb are current positions of Alma and Berthe
    // respectively
    // boolean Alma means whether it is Alma's turn
    // System.out.println(ra + ":" + pa + "," + rb + ":" + pb + Alma + ":" + score);
    List<Integer[]> al = getAvailRooms(ra, pa, S, avail);
    List<Integer[]> bl = getAvailRooms(rb, pb, S, avail);

    if (al.size() == 0 && bl.size() == 0) {
      // return current score
      return score;
    }

    int result;
    if (Alma == true) {
      // Alma' turn, try each available room
      if (al.size() == 0) {
        // turn to Berthe
        return dfs(ra, pa, rb, pb, S, score, avail, !Alma, alpha, beta);
      }
      result = Integer.MIN_VALUE;
      for (Integer[] l : al) {
        avail[l[0]][l[1]] = false;
        int temp = dfs(l[0], l[1], rb, pb, S, score+1, avail, !Alma, alpha, beta);
        avail[l[0]][l[1]] = true;
        result = Math.max(result, temp);
        alpha = Math.max(alpha, temp);
        if (beta <= alpha) {
          break;
        }

      }
    } else {
      // Berthe's turn
      if (bl.size() == 0) {
        // turn to Alma
        return dfs(ra, pa, rb, pb, S, score, avail, !Alma, alpha, beta);
      }
      result = Integer.MAX_VALUE;
      for (Integer[] l : bl) {
        avail[l[0]][l[1]] = false;
        int temp = dfs(ra, pa, l[0], l[1], S, score-1, avail, !Alma, alpha, beta);
        avail[l[0]][l[1]] = true;
        result = Math.min(result, temp);
        beta = Math.min(beta, temp);
        if (beta <= alpha) {
          break;
        }
      }
    }

    return result;
  }

  public static List<Integer[]> getAvailRooms(int i, int j, int S, boolean[][] avail) {
    // return list of neighbor positions
    List<Integer[]> res = new ArrayList<>();
    if (j%2 == 0) {
      // if second index is even, it connects to upper row
      if (avail[i-1][j-1]) res.add(new Integer[] {i-1, j-1});
    } else {
      // it connects to bottom row
      if (i+1 <= S) {
        if (avail[i+1][j+1]) res.add(new Integer[] {i+1, j+1});
      }
    }

    // left and right room
    if (j-1 >= 1 && avail[i][j-1]) res.add(new Integer[] {i, j-1});
    if (j+1 < 2*i && avail[i][j+1]) res.add(new Integer[] {i, j+1});
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
