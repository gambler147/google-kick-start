import java.io.*;
import java.util.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // priority queue. Start from the highest cells to lowest
    int N = Integer.parseInt(br.readLine());
    String[] s;
    // read A
    int[][] A = new int[N][N];
    for (int i=0; i<N; i++) {
      s = br.readLine().split(" ");
      for (int j=0; j<N; j++) {
        A[i][j] = Integer.parseInt(s[j]);
      }
    }

    // read B
    int[][] B = new int[N][N];
    for (int i=0; i<N; i++) {
      s = br.readLine().split(" ");
      for (int j=0; j<N; j++) {
        B[i][j] = Integer.parseInt(s[j]);
      }
    }

    // read checksum of R
    int[] R = new int[N];
    s = br.readLine().split(" ");
    for (int i=0; i<N; i++) {
      R[i] = Integer.parseInt(s[i]);
    }
    // read checksum of C
    int[] C = new int[N];
    s = br.readLine().split(" ");
    for (int i=0; i<N; i++) {
      C[i] = Integer.parseInt(s[i]);
    }

    // to solve the problem, we can think of the -1 value in A is a conjunction of a row and col, 
    // and we link the row and col as a edge of a graph. And in the end, we have a bipartite graph.
    // notice that if the bipartite graph does not contain any cycle, then we do not need to break any edge
    // and we do not need to recover A. So the problem becomes detecting cycles and breaking cycles with 
    // minimum weights

    HashMap<Integer, List<Integer>> graph = new HashMap<>();
    for (int i=0; i<2*N; i++) {
      graph.put(i, new ArrayList<>());
    }
    for (int i=0; i<N; i++) {
      for (int j=0; j<N; j++) {
        if (A[i][j] == -1) {
          graph.get(i).add(N+j);
          graph.get(N+j).add(i);
        }
      }
    }

    // solve
    int res = 0;
    HashSet<Integer> visited = new HashSet<>();
    for (int i=0; i<N; i++) {
      if (!visited.contains(i)) {
        res += minimumCostToBreakGraph(i, graph, B, visited);
      }
    }

    System.out.format("Case #%d: %d%n", trial, res);
    
  }

  public static int minimumCostToBreakGraph(int i, Map<Integer, List<Integer>> graph, int[][] B, Set<Integer> visited) {
    // i is row. 0 <= i < N

    int N = B.length;
    
    Set<Integer> nodes = new HashSet<>();
    Stack<Integer> stack = new Stack<>();

    stack.push(i);
    int total_weights = 0;
    // find sub-graph nodes
    while (!stack.isEmpty()) {
      int node = stack.pop();
      nodes.add(node);
      for (int o : graph.get(node)) {
        if (!nodes.contains(o)) {
          stack.push(o);
          if (node < N) {
            total_weights += B[node][o-N];
          } else {
            total_weights += B[o][node-N];
          }
        }
      }
    }

    visited.addAll(nodes);
    // System.out.println(nodes);

    // Prim's algorithm for maximum spanning tree
    int[] dist = new int[2*N]; // distance to current graph for each node outside the graph
    for (int j=0; j<2*N; j++) {
      dist[j] = 0;
    }
    int max_spanning_tree_weights=0;
    // start from node i
    nodes.remove(i);
    for (int node : nodes) {
      if (node >= N) {
        dist[node] = B[i][node-N];
      }
    }

    while (!nodes.isEmpty()) {
      int target_node = -1;
      int max_dist = -1;
      for (int node : nodes) {
        if (dist[node] > max_dist) {
          target_node = node;
          max_dist = dist[node];
        }
      }

      // add this node to graph
      max_spanning_tree_weights += max_dist;
      nodes.remove(target_node);

      // update dist
      for (int node : nodes) {
        if (node < N && target_node >= N) {
          dist[node] = Math.max(dist[node], B[node][target_node-N]);
        } else if (node >= N && target_node < N) {
          dist[node] = Math.max(dist[node], B[target_node][node-N]);
        }
      }
    }

    // System.out.println(total_weights);
    // System.out.println(max_spanning_tree_weights);
    return total_weights - max_spanning_tree_weights;
    
    
  }


  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    int test = Integer.parseInt(br.readLine());

    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
