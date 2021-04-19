import java.io.*;
import java.util.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // first line: N, Q
    String[] s = br.readLine().split(" ");
    int N = Integer.parseInt(s[0]);
    int Q = Integer.parseInt(s[1]);
    // initialize map, limit map, charge map
    Map<Integer, Set<Integer>> links = new HashMap<>();
    for (int i=1; i<=N; i++) {
      links.put(i, new HashSet<>());
    }
    Map<List<Integer>, Integer> limit = new HashMap<>();
    Map<List<Integer>, Long> charge = new HashMap<>();
  
    int max_limit = 0;
    // next N-1 lines: (City_X, City_Y, Limit, Charge)
    for (int i=1; i<N; i++) {
      s = br.readLine().split(" ");
      int X = Integer.parseInt(s[0]);
      int Y = Integer.parseInt(s[1]);
      int L = Integer.parseInt(s[2]);
      long A = Long.parseLong(s[3]);
      links.get(X).add(Y);
      links.get(Y).add(X);
      limit.put(Arrays.asList(X,Y), L);
      limit.put(Arrays.asList(Y,X), L);
      charge.put(Arrays.asList(X,Y), A);
      charge.put(Arrays.asList(Y,X), A);
      max_limit = Math.max(max_limit, L);
    }

    // next Q lines: (City, Weight) we will group the queries by city and sorted by weight
    Map<Integer, List<List<Integer>>> query = new HashMap<>(); // the element is (weight, original index)
    for (int i=1; i<=N; i++) {
      query.put(i, new ArrayList<>());
    }
    for (int i=0; i<Q; i++) {
      s = br.readLine().split(" ");
      int C = Integer.parseInt(s[0]);
      int W = Integer.parseInt(s[1]);
      max_limit = Math.max(max_limit, W);
      query.get(C).add(Arrays.asList(W, i));
    }
    // // we sort for each query
    // for (int i=1; i<=N; i++) {
    //   Collections.sort(query.get(i), (a, b) -> a.get(0) - b.get(1));
    // }    
    
    // we now construct a gcd segment tree of size max_limit
    int n = max_limit+1;
    long[] arr = new long[n];
    SegmentTree st = new SegmentTree(arr);

    /* we then do a dfs starting from root 1 and at each node visited, 
     for arriving each node, 
     1. we first update current edge's limit to stree by calling stree.updateValue(arr, n, C, L)
     2. we then go through every query[node] to get gcd by calling stree.getGcd(n, 1, W)
     check the sorted query[node], calling stree.getGcd(n, 1, W)
      */
    long[] res = new long[Q];
    
    // stack<cur node, parent node>
    Stack<List<Integer>> stack = new Stack<>();
    stack.push(Arrays.asList(1, -1));

    while (stack.size() > 0) {
      List<Integer> l = stack.peek();
      int cur = l.get(0);
      int pre = l.get(1);

      // check if this node has any child
      if (links.get(cur).size() > 0) {
        Iterator<Integer> iterator = links.get(cur).iterator();
        int child = iterator.next();
        // pop last element from links
        links.get(cur).remove(child);
        links.get(child).remove(cur);
        // get Limit and Charge
        int L = limit.get(Arrays.asList(cur, child));
        long A = charge.get(Arrays.asList(cur, child));
        // update stree
        st.updateValue(arr, n, L, A);
        // then we do queries for child
        processQuery(child, query.get(child), n, st, res);

        // append current child to stack
        stack.push(Arrays.asList(child, cur));

        // System.out.format("%d %d %d", child, L, A);
        // System.out.println(Arrays.toString(st.st));
        continue;
      }

      // otherwise, we remove current node from stack
      if (pre != -1) {
        int L = limit.get(Arrays.asList(cur, pre));
        st.updateValue(arr, n, L, 0);
      }

      stack.pop();
    }

    // print result
    StringBuilder sb = new StringBuilder();
    for (long r : res) {
      sb.append(r);
      sb.append(" ");
    }

    System.out.format("Case #%d: %s%n", trial, sb.toString());
    

  }
  public static void processQuery(int child, List<List<Integer>> query, int n, SegmentTree st, long[] res ) {
    // child is the current city, arr is the segment tree object and res is the results
    // n == max_limit+1 is length of arr
    for (List<Integer> l : query) {
      int W = l.get(0);
      int idx = l.get(1);
      long gcd = st.getGcd(n, 1, W);
      res[idx] = gcd;
    }
    return;
  }


  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int test = Integer.parseInt(br.readLine());
    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}


// Java implementation of the approach
class SegmentTree {
    
  // segment tree
  public long st[];

  public SegmentTree(long[] arr) {
    int n = arr.length;
    constructGcd(arr, n);
  }

  // Recursive function to return gcd of a and b
  static long __gcd(long a, long b)
  {
    if (b == 0 || a == 0) 
      return a+b;
    return __gcd(b, a % b);
    
  }

  // A utility function to get the
  // middle index from corner indexes
  static int getMid(int s, int e) {
    return (s + (e - s) / 2);
  }

  private long getGcdUtil( int ss, int se, int qs, int qe, int si)
  {
    // then return the gcd of the segment
    if (qs <= ss && qe >= se)
      return st[si];

    // If segment of this node is outside the given range
    if (se < qs || ss > qe)
      return 0;

    // If a part of this segment overlaps with the given range
    int mid = getMid(ss, se);
    return __gcd(getGcdUtil( ss, mid, qs, qe, 2 * si + 1),
          getGcdUtil( mid + 1, se, qs, qe, 2 * si + 2));
  }

  private long updateValueUtil( int ss, int se, int i, long new_val, int si) {
    // Base Case: If the input index lies outside the range of
    // this segment
    if (i < ss || i > se)
      return st[si];

    // If the input index is in range of this node, then update
    // the value of the node and its children
    // st[si] = st[si] + diff;
    if (se == ss) {
      st[si] = new_val;
    } else {
      int mid = getMid(ss, se);
      st[si] = __gcd(updateValueUtil( ss, mid, i, new_val, 2 * si + 1),
                  updateValueUtil( mid + 1, se, i, new_val, 2 * si + 2));
    }
    return st[si];
  }

  // The function to update a value in input array and segment tree.
  // It uses updateValueUtil() to update the value in segment tree
  public void updateValue(long arr[], int n, int i, long new_val)
  {
    // Check for erroneous input index
    if (i < 0 || i > n - 1) {
      System.out.println("Invalid Input");
      return;
    }

    // Update the value in array
    arr[i] = new_val;

    // Update the values of nodes in segment tree
    updateValueUtil( 0, n - 1, i, new_val, 0);
  }

  // Function to return the sum of elements in range
  // from index qs (query start) to qe (query end)
  // It mainly uses getSumUtil()
  public long getGcd( int n, int qs, int qe)
  {

    // Check for erroneous input values
    if (qs < 0 || qe > n - 1 || qs > qe)
    {
      System.out.println( "Invalid Input");
      return -1;
    }

    return getGcdUtil( 0, n - 1, qs, qe, 0);
  }

  // A recursive function that constructs Segment Tree for array[ss..se].
  // si is index of current node in segment tree st
  private long constructGcdUtil(long arr[], int ss, int se, int si) {
    // If there is one element in array, store it in current node of
    // segment tree and return
    if (ss == se)
    {
      st[si] = arr[ss];
      return arr[ss];
    }

    // If there are more than one element then recur for left and
    // right subtrees and store the sum of values in this node
    int mid = getMid(ss, se);
    st[si] = __gcd(constructGcdUtil(arr, ss, mid, si * 2 + 1),
          constructGcdUtil(arr, mid + 1, se, si * 2 + 2));
    return st[si];
  }

  // Function to construct segment tree from given array. This function
  // allocates memory for segment tree and calls constructSTUtil() to
  // fill the allocated memory
  public void constructGcd(long arr[], int n) {
    // Allocate memory for the segment tree

    // Height of segment tree
    int x = (int)(Math.ceil(Math.log(n)/Math.log(2)));

    // Maximum size of segment tree
    int max_size = 2 * (int)Math.pow(2, x) - 1;

    // Allocate memory
    st = new long[max_size];

    // Fill the allocated memory st
    constructGcdUtil(arr, 0, n - 1, 0);

  }

}