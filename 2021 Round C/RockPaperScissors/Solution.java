// https://codingcompetitions.withgoogle.com/kickstart/round/0000000000435c44/00000000007ec28e

import java.io.*;
import java.util.*;

/**
 * The strategy is to use dynamic programming.
 * Assume we are going for round r+1 and we we have picked i rocks, j scissors and k papers
 * from previous r rounds so i+j+k = r. To determine the next pick (r+1) based on maximum
 * expected score, lets define it to be dp[i][j][k]. There are 3 options:
 *  1. pick rock, which gives an expected score of dp[i+1][j][k] + W*k/r + E*j/r;
 *  2. pick scissors, which gives an expected score of dp[i][j+1][k] + W*i/r + E*k/r;
 *  3. pick scissors, which gives an expected score of dp[i][j+1][k] + W*j/r + E*i/r;
 * (if i==j==k==0, the last two terms will be 1/3*W + 1/3*E for all three cases)
 * So we pick the option which gives maximum expected score. And the initial condition
 * is dp[i][j][k] = 0 for i+j+k = 60. 
 * Then we iterate i,j,k from 60 to 0 and i+j+k < 60.
 * We also use an additional array to keep track of best strategy for each tuple (i,j,k)
 * to construct the final decision string.
 */
public class Solution {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // try R, S, P 
    String[] s = br.readLine().split(" ");
    int W = Integer.parseInt(s[0]);
    int E = Integer.parseInt(s[1]);
    int T = 60; // rounds per day
    // using dp. dp[i][j][k] is the maximum expected score given already 
    // i rocks, j scissors and k papers
    double[][][] dp = new double[T+1][T+1][T+1];
    // Strategy of each round given ri = i, si = j, pi = k. value is 0, 1 or 2, correspoinding
    // to rock and scissor and paper.
    int[][][] dir = new int[T][T][T]; 
    // 1. i + j + k <= T
    // 2. dp[i][j][k] = 0 for i + j + k == T
    for (int i=T-1; i>=0; i--) {
      for (int j=T-i-1; j>=0; j--) {
        for (int k=T-i-j-1; k>=0; k--) {
          int round = i+j+k;
          double rock_score = dp[i+1][j][k];
          double scissor_score = dp[i][j+1][k];
          double paper_score = dp[i][j][k+1];
          if (round == 0) {
            double v =  1.0/3 * W + 1.0/3 * E;
            rock_score += v;
            scissor_score += v;
            paper_score += v;
          } else {
            rock_score += W * 1.0*k/round + E * 1.0*j/round;
            scissor_score += W * 1.0*i/round + E*1.0*k/round;
            paper_score += W * 1.0*j/round + E*1.0*i/round;
          }
          // determine best strategy
          if (rock_score >= scissor_score && rock_score >= paper_score) {
            dp[i][j][k] = rock_score;
            dir[i][j][k] = 0;
          } else if (scissor_score >= rock_score && scissor_score >= paper_score) {
            dp[i][j][k] = scissor_score;
            dir[i][j][k] = 1;
          } else {
            dp[i][j][k] = paper_score;
            dir[i][j][k] = 2;
          }
        }
      }
    }

    // reconstruct best strategy
    char[] rsp = new char[] {'R','S','P'};
    StringBuilder sb = new StringBuilder("");
    int[] idx = new int[] {0, 0, 0};
    for (int c=0; c<T; c++) {
      int i = idx[0];
      int j = idx[1];
      int k = idx[2];
      sb.append(rsp[dir[i][j][k]]);
      idx[dir[i][j][k]] += 1;
    }
    System.out.format("Case #%d: %s%n", trial, sb.toString());
  }

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int test = Integer.parseInt(br.readLine());
    int X = Integer.parseInt(br.readLine());
    for (int trial=1; trial <= test; trial++) {
      solve(trial, br);
    }
  }
}
