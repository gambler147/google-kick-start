import java.io.*;

public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    int n = Integer.parseInt(br.readLine());
    String[] s = br.readLine().split(" ");
    // initialize array
    int[] arr = new int[n];
    for (int i=0; i<n; i++) {
      arr[i] = Integer.parseInt(s[i]);
    }
    // find algorithmic subarray
    int res=0;
    res = helper(n, arr);
    reverse(arr);
    res = Math.max(res, helper(n, arr));

    System.out.format("Case #%d: %d%n", trial, res);
    
  }

  public static void reverse(int[] arr) {
    int n = arr.length;
    for (int i=0; i<n/2; i++) {
      int t = arr[i];
      arr[i] = arr[n-i-1];
      arr[n-i-1]=t;
    }
  }
  public static int helper(int n, int[] arr) {
    // if n <= 3 return n
    if (n <= 3) {
      return n;
    }
    
    int[] gap = new int[n-1];
    for (int i=0; i<n-1; i++) {
      gap[i] = arr[i+1] - arr[i];
    }

    // loop through gap, use two pointers
    int res=3;
    int i=0;
    int j=0;
    int inc=0; // num increased for a given value
    int cur=1;
    while (i < n-1) {
      // check if current value = prev value
      if (i == 0 || gap[i] == gap[i-1]) {
        res = Math.max(res, ++cur);
      } else if (inc == 0) {
        // we change current to match with previous element
        inc = gap[i] - gap[i-1];
        gap[i]-=inc;
        if (i+1 < n-1) {
          gap[i+1] += inc;
        }
        j = i;
        res = Math.max(res, ++cur);
      } else {
        // we have changed element already, we have to revert back
        // recover jth and j+1th element
        gap[j]+=inc;
        if (j+1 < n-1) {
          gap[j+1]-=inc;
        }
        cur = 2;
        i=j;
        inc=0;
      }
      i++;
    }
    res = Math.max(res, cur);
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
