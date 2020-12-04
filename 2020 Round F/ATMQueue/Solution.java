import java.util.*;
import java.io.*;

public class ATMQueue {
  public static void solve(int trial, BufferedReader br) throws IOException {
    String[] s = br.readLine().split(" ");
    int n = Integer.parseInt(s[0]); // number of people
    int X = Integer.parseInt(s[1]); // maximum amount of money can be withdrawn

    // read array
    s = br.readLine().split(" ");
    PriorityQueue<Integer[]> pq = new PriorityQueue<>((a, b) -> {
      if (a[0] < b[0]) return -1;
      else if (a[0] > b[0]) return 1;
      else return a[1] - b[1];
    });

    for (int i=0; i<n; i++) {
      int amount = Integer.parseInt(s[i]);
      int a = amount%X == 0 ? amount/X : amount/X + 1;
      pq.add(new Integer[] {a, i+1});
    }

    StringBuilder sb = new StringBuilder("Case #" + String.valueOf(trial) + ":");
    while (pq.size() > 0) {
      Integer[] l = pq.poll();
      sb.append(" " + String.valueOf(l[1]));
    }

    System.out.println(sb.toString());
    
  }
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int test = Integer.parseInt(br.readLine());

    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}