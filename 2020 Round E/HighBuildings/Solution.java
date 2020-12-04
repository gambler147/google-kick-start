/*
Problem In an unspecified country, Google has an office campus consisting of
N office buildings in a line, numbered from 1 to N from left to right. When
represented in meters, the height of each building is an integer between 1 to
N, inclusive.

Andre and Sule are two Google employees working in this campus. On their
lunch break, they wanted to see the skyline of the campus they are working
in. Therefore, Andre went to the leftmost point of the campus (to the left of
building 1), looking towards the rightmost point of the campus (to the right
of building N). Similarly, Sule went to the rightmost point of the campus,
looking towards the leftmost point of the campus.

To Andre, a building x is visible if and only if there is no building to the
left of building x that is strictly higher than building x. Similarly, to
Sule, a building x is visible if and only if there is no building to the
right of building x that is strictly higher than building x.

Andre learned that there are A buildings that are visible to him, while Sule
learned that there are B buildings that are visible to him. After they
regrouped and exchanged information, they also learned that there are C
buildings that are visible to both of them.

They are wondering about the height of each building. They are giving you the
value of N, A, B, and C for your information. As their friend, you would like
to construct a possible height for each building such that the information
learned on the previous paragraph is correct, or indicate that there is no
possible height construction that matches the information learned (thus at
least one of them must have been mistaken).

Input The first line of the input gives the number of test cases, T. T test
cases follow. Each consists of a single line with four integers N, A, B, and
C: the information given by Andre and Sule.

Output For each test case, output one line containing Case #x: y, where x is
the test case number (starting from 1) and y is IMPOSSIBLE if there is no
possible height for each building according to the above information, or N
space-separated integers otherwise. The i-th integer in y must be the height
of the i-th building (in meters) between 1 to N.

Limits Time limit: 20 seconds per test set. Memory limit: 1GB. 1 ≤ T ≤ 100. 1
≤ C ≤ N. C ≤ A ≤ N. C ≤ B ≤ N.

Test Set 1 1 ≤ N ≤ 5.

Test Set 2 1 ≤ N ≤ 100.
*/

import java.io.*;

class highBuildings {
  public static int[] solve(int N, int A, int B, int C) {
    // if A+B-C > N, then there is no possible height construction
    if (A+B-C > N || (A+B-C == 1 && N >= 2)) {
      return new int[] {-1};
    } else if (N == 1) {
      return new int[] {1};
    } else if (N == 2) {
      if (C == 2) {
        return new int[] {1,1};
      } else if (A == 2) {
        return new int[] {1,2};
      } else if (B == 2) {
        return new int[] {2,1};
      } else {
        return new int[] {-1};
      }
    }

    int[] res = new int[N];
    int l = A-C;
    int r = B-C;

    for (int i=0; i<l; i++) {
      res[i] = 2;
    }

    for (int i=l; i<l+C; i++) {
      res[i] = 3;
    }

    for (int i=l+C; i<l+C+r; i++) {
      res[i] = 2;
    }

    int k = A+B-C;
    int extra = N - k;

    if (extra > 0) {
      // shift the array
      for (int i=N-1; i>extra; i--) {
        res[i] = res[i-extra];
      }

      for (int i=1; i<extra+1; i++){
        res[i] = 1;
      } 
    }

    return res;
    
  }
  public static void main(String[] args) throws NumberFormatException, IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int test = Integer.parseInt(br.readLine());
    for (int t = 1; t<=test; t++) {
      String[] s = br.readLine().split(" ");
      int N = Integer.parseInt(s[0]);
      int A = Integer.parseInt(s[1]);
      int B = Integer.parseInt(s[2]);
      int C = Integer.parseInt(s[3]);

      int[] arr = solve(N,A,B,C);
      String ans;
      if (arr[0] == -1) {
        ans = "IMPOSSIBLE";
      } else {
        StringBuilder sb = new StringBuilder();
        sb.append(arr[0]);
        for (int i=1; i<arr.length; i++) {
          sb.append(" ").append(arr[i]);
        }
        ans = sb.toString();
      }

      System.out.format("Case #%d: %s%n", t, ans);
    }
  }  
}