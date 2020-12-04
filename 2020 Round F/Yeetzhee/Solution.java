import java.io.*;
import java.util.*;

public class Yeetzhee {
  public static List<Integer> target;
  public static int M;
  public static int N;
  public static int K;
  public static Map<List<Integer>, Double> memo;
  public static void solve(int trial, BufferedReader br) throws IOException {
    String[] s = br.readLine().split(" ");
    N = Integer.parseInt(s[0]);
    M = Integer.parseInt(s[1]);
    K = Integer.parseInt(s[2]);

    target = new ArrayList<>();
    // read array
    for (int i=0; i<M-K; i++) target.add(0);
    for (int i=M-K; i<M; i++) {
      target.add(Integer.parseInt(br.readLine()));
    }

    memo = new HashMap<>();
    memo.put(target, 0.0);

    List<Integer> arr = new ArrayList<>(); 
    for (int i=0; i<M; i++) arr.add(0);

    double res = dfs(arr);
    // double res = dp(M, K, arr, counter);
    System.out.format("Case #%d: %f%n", trial, res);
  }

  public static double dfs(List<Integer> arr) {
    if (memo.containsKey(arr)) return memo.get(arr);
    int choice = 0;
    double res = 0;
    int i = 0;
    while (i < M) {
      int j = i;
      while (j+1 < M && arr.get(j+1) == arr.get(i)) {
        j++;
      }
      if (arr.get(j) + 1 <= target.get(j)) {
        arr.set(j, arr.get(j)+1);
        choice += j-i+1;
        res += dfs(arr) * (j-i+1);
        arr.set(j, arr.get(j)-1);
      }
      i = j+1;
    }
    // normalize. EV = (M-pos)/M*EV + pos/M*EV' -> EV = M/pos * EV'*M/pos
    res = res/choice + 1.0*M/choice;
    memo.put(arr, res);
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
