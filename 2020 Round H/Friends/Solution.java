import java.io.*;
import java.util.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    String[] s = br.readLine().split(" ");
    int N = Integer.parseInt(s[0]);
    int Q = Integer.parseInt(s[1]);

    // graph of letters
    List<Set<Integer>> graph = new ArrayList<>();
    for (int i=0; i<26; i++) {
      graph.add(new HashSet<>());
    }
    // read names, using sets to store
    List<Set<Integer>> nameSets = new ArrayList<>();
    s = br.readLine().split(" ");
    for (int i=0; i<N; i++) {
      Set<Integer> nset = new HashSet<>();
      for (char c : s[i].toCharArray()) {
        nset.add(c - 'A');
      }
      nameSets.add(nset);

      Integer[] arr = nset.toArray(new Integer[nset.size()]);
      // construct graph
      for (int j=0; j<arr.length-1; j++) {
        for (int k=j+1; k<arr.length; k++) {
          int a = arr[j];
          int b = arr[k];
          graph.get(a).add(b);
          graph.get(b).add(a);
        }
      }
    }

    StringBuilder sb = new StringBuilder();

    for (int i=0; i<Q; i++) {
      s = br.readLine().split(" ");
      int l = Integer.parseInt(s[0]);
      int r = Integer.parseInt(s[1]);
      int res = helper(l, r, nameSets, graph);
      sb.append(" ");
      sb.append(res);
    }

    // for each query, we do bfs to check
    System.out.format("Case #%d:" + sb.toString() + "%n", trial);

  }

  public static int helper(int i, int j, List<Set<Integer>> nameSets, List<Set<Integer>> graph) {
    // returns minimum chain from person i to person j
    i -= 1;
    j -= 1;
    int res = 1;

    Set<Integer> seen = new HashSet<>();
    List<Integer> bfs = new ArrayList<>();
    for (int p : nameSets.get(i)) {
      bfs.add(p);
      seen.add(p);
    }
    
    while (!bfs.isEmpty()) {
      res++;
      List<Integer> next = new ArrayList<>();
      for (int p : bfs) {
        // if p is in namesSets[j]
        if (nameSets.get(j).contains(p)) {
          return res;
        }
        // other wise, add p's neighbors to next bfs list
        for (int q : graph.get(p)) {
          if (!seen.contains(q)) {
            next.add(q);
            seen.add(q);
          }
        }
      }
      bfs = next;
    }
    return -1;
  }
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int test = Integer.parseInt(br.readLine());

    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
