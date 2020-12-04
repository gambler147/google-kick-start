import java.io.*;
import java.util.*;

public class MetalHarvest {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // greedy
    String[] s = br.readLine().split(" ");
    int N = Integer.parseInt(s[0]); // number of intervals
    int K = Integer.parseInt(s[1]); // maximum duration a robot can be deployed for before returning for calibration

    int[][] arr = new int[N][2];
    for (int i=0; i<N; i++) {
      s = br.readLine().split(" ");
      arr[i][0] = Integer.parseInt(s[0]);
      arr[i][1] = Integer.parseInt(s[1]);
    }

    // sort array based on start time
    Arrays.sort(arr, (a,b) -> (a[0] - b[0]));
    int res = 0;
    int last=-1; // end time of last robot

    for (int i=0; i<N; i++) {
      int l = arr[i][0];
      int r = arr[i][1];
      if (last > l) {
        l = last;
      }
  
      // if last robot's deployment have already covered this, we can skip this
      if (l >= r) continue;
  
      // otherwise, we cover the rest interval using multiple K intervals
      int c = (r-l)%K == 0 ? (r-l)/K : (r-l)/K + 1;
      res += c;
      last = r + (c*K) - (r-l); // extra end point current deployment can cover
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
