// Problem Little Axel has N toys numbered from 1 to N. Each toy has two
// properties:

// Ei—enjoyment, which is the number of minutes Axel can play with toy number i
// without getting bored with it; Ri—remembrance, which is the number of minutes
// it takes Axel to forget toy number i after having played with it. The toys
// are arranged in a circle, from 1 to N clockwise. Axel plays with them one by
// one.

// When Axel reaches toy i which he has not played with yet, or which he has
// already forgotten about, he plays with it for Ei minutes and then immediately
// moves to the next one (clockwise).

// If he reaches a toy that he has not forgotten yet (if less than Ri minutes
// have passed since the last time he finished playing with it), he will stop
// and cry.

// We can define the time Axel spent playing as the sum of Ei of every toy Axel
// played with before stopping. If Axel played with a toy several times, it
// should be counted that many times.

// Given the description of the toys, remove the smallest possible number of
// them in order to make Axel play either an indefinitely long time, or (if that
// is not possible) as long as possible before he stops.

// Note:

// Axel has never played with these toys before; he cannot be left without toys;
// he always starts with the toy that has the smallest number; after finishing
// playing with the toy that has the largest number, he will move to the toy
// that has the smallest number. Input The first line of the input gives the
// number of test cases, T. T test cases follow. Each test case begins with a
// line containing the integer N. Next N lines contain 2 integers each: Ei and
// Ri. The i-th line is describing the toy number i.

// Output For each test case, output one line containing Case #x: y z, where:

// x is the test case number (starting from 1); y is the minimal number of toys
// to remove so that Axel could play with the rest of them either indefinitely
// or as long as possible; z is the longest time Axel will play in minutes or
// "INDEFINITELY" (without quotes) if he will play indefinitely long time.

import java.io.*;
import java.util.*;


public class toys {
  public static void solve(int n, int[] E, int[] R) {

    // use a priority queue to record Ei + Ri, with larger number at the front of queue
    PriorityQueue<List<Integer>> pQueue = new PriorityQueue<>((a,b) -> b.get(0) - a.get(0));

    // important variables
    long sum = 0; // sum of enjoyment of all toys, should use long instead of int because there can be overflow issue for E[i] <= 10**9
    int removed = 0; // number of removed toys
    int count = 0; // temperary removed element

    // first loop, calculate sum of enjoyment and put all Ei + Ri to the queue
    for (int i=0; i<n; i++) {
      sum += E[i];
    }
    long cur_time = sum; // current time axel is playing
    long max_time = sum; // maximum time axel can play

    // second loop
    for (int i=0; i<n; i++) {
      List<Integer> l = new ArrayList<>();
      l.add(E[i]+R[i]);
      l.add(i);
      pQueue.add(l);
      cur_time += E[i];

      while (!pQueue.isEmpty()) {
        List<Integer> top = pQueue.peek();
        if (sum >= top.get(0)) {
          // if top element satisfies this condition, meaning so far axel can still play
          break;
        } else {
          // other wise, axel has encountered some toy that he has to stop, so remove this toy
          int k = top.get(1);
          cur_time -= E[k] * 2;
          sum -= E[k];
          count += 1;
          pQueue.poll();
        }
      }

      // update max_time and removed
      if (cur_time > max_time) {
        max_time = cur_time;
        removed = count;
      } else if (cur_time == max_time) {
        // if two outcomes have same 
        removed = Math.min(removed, count);
      }

    }
    // if priority queue is not empty, meaning toys in the queue can be played indefinitely
    if (!pQueue.isEmpty()) {
      System.out.print(count + " " + "INDEFINITELY");
    } else {
      System.out.print(removed + " " + max_time);
    }

    return;
  }
  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int test = Integer.parseInt(br.readLine());
    for (int t = 1; t<=test; t++) {
      int n = Integer.parseInt(br.readLine()); // number of toys
      int[] E = new int[n]; // entertainment array
      int[] R = new int[n]; // rememberence array

      // read array
      for (int i=0; i<n; i++) {
        String[] s = br.readLine().split(" ");
        E[i] = Integer.parseInt(s[0]);
        R[i] = Integer.parseInt(s[1]);
      }

      // solve and print
      System.out.format("Case #%d: ", t);
      solve(n, E, R);
      System.out.print("\n");
      
    }

    br.close();
  }
}