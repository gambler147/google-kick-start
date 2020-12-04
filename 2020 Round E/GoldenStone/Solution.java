// Problem Leopold's friend Kate likes stones, so he decided to give her a
// golden stone as a gift. There are S types of stones numbered from 1 to S, 1
// being the golden stone. Some types of stones are available free of charge in
// various parts of the city. The city consists of N junctions numbered from 1
// to N and M two-way streets between pairs of distinct junctions. At each
// junction, zero or more types of stones are available in unlimited supply.

// Unfortunately, the golden stone is not available anywhere. Luckily, Leopold
// is a bit of a magician and knows how to combine a group of stones and turn
// them into another stone. For example, one of his recipes could produce a
// golden stone out of one silver stone and two marble stones. He could collect
// those stones in some of the junctions if they are available, or he could use
// some of his many other recipes to produce any of those stones. Formally,
// Leopold has R recipes, where a recipe is in the form (a1, a2, ..., ak) -> b
// for some k ≥ 1. If Leopold has gathered k stones of types a1, a2, ..., and ak
// at a certain junction, he can choose to apply the recipe and turn these
// stones into one stone of type b.

// Leopold likes puzzles much more than physical activity, therefore, he does
// not want to carry stones around the city unnecessarily. Carrying a stone
// along a street costs him one unit of energy. Leopold can carry no more than
// one stone at a time, however, he can drop off a stone at any junction and
// pick it up later at any time.

// What is the minimum amount of energy Leopold must spend to produce one golden
// stone? Leopold is very scared of large numbers. If the answer is greater than
// or equal to 1012, print -1 instead.

// Input The first line of the input gives the number of test cases T. T test
// cases follow. The first line of each test case consists of four integers N,
// M, S, and R giving the number of junctions, streets, stone types, and
// recipes, respectively. The following M lines describe the map of the city.
// The i-th of these lines contains two distinct integers Ui and Vi denoting the
// pair of junctions connected by the i-th street.

// The subsequent N lines describe the types of stones available in each
// junction. The i-th of these lines starts with the number of stone types Ci
// available in i-th junction followed by Ci distinct integers in the range [2,
// S] enumerating the stone types. The golden stone is always numbered 1 and is
// not available.

// The last R lines of each test case describe Leopold's magic recipes. The i-th
// of these lines starts with the number of ingredient stones Ki required for
// the i-th recipe followed by Ki not necessarily distinct integers in the range
// [2, S] enumerating the types of necessary ingredients. The i-th line ends
// with an integer in the range [1, S], which is the type of the resulting stone
// after applying the i-th recipe. For example 3 6 5 6 3 denotes a recipe that
// requires two stones of type 6, one stone of type 5, and produces a stone of
// type 3.

// It is guaranteed that it is possible to produce a golden stone in each of the
// test cases.

// Output For each test case, output one line containing Case #x: y, where x is
// the test case number (starting from 1) and y is the answer for the test case
// x, namely, the minimum amount of energy Leopold must spend to produce one
// golden stone. If the answer is greater than or equal to 1012, print -1
// instead.


import java.io.*;
import java.util.*;

class goldenStone {
  public static void solve(int trial, BufferedReader br) throws IOException {
    // read N, M, S, R
    int N, M, S, R;
    String[] s = br.readLine().split(" ");
    N = Integer.parseInt(s[0]);
    M = Integer.parseInt(s[1]);
    S = Integer.parseInt(s[2]);
    R = Integer.parseInt(s[3]);

    Map<Integer, List<Integer>> graph = new HashMap<>();  // graph[i] contains all neighbor junctions of junction i
    for (int i=1; i<=N; i++) {
      graph.put(i, new ArrayList<>());
    }
    // Map<Integer, List<Integer>> junction_to_stones = new HashMap<>(); // junction_to_stones[i] contains all stones junction i has
    Map<Integer, List<Integer>> stone_to_rid = new HashMap<>(); // for each stone ingredients we records list of recipes it involved
    for (int i=1; i<=S; i++) {
      stone_to_rid.put(i, new ArrayList<>());
    }
    int[] rid_to_results = new int[R+1];  // each rid -> resulting stone type
    Map<Integer, Map<Integer, Integer>> rid_to_recipes = new HashMap<>(); // contains all recipe ids to product a stone type of i, pair.first is stone_type, 
    // pair.second is number needed for stone_type

    // create a table cost[junction][stone_type], which stores the minimum cost for carrying stone_type to junction
    long[][] cost = new long[N+1][S+1];
    long limit = (long) 1e12;
    for (int i=1; i<=N; i++) {
      for (int j=1; j<=S; j++) {
        cost[i][j] = limit;
      }
    }
    // pq's element should be <energy_use, <junction, stone_type>> whose energy_use is the smallest
    PriorityQueue<Pair<Long, Pair<Integer, Integer>>> pq = new PriorityQueue<>((a,b) -> (int) (a.first - b.first)); 

    // following M lines are the graph of this city, we use a map to record graph {from (int): to (List)}, cities are numbered from 1 to N
    for (int i = 0; i<M; i++) {
      s = br.readLine().split(" "); // two elements
      int u = Integer.parseInt(s[0]);
      int v = Integer.parseInt(s[1]);
      graph.get(u).add(v);
      graph.get(v).add(u);
      
    }
    
    // next N lines are stones types each junction has, we use a set to record {stone type(int): junctions(List<Integer>)}
    for (int junction=1; junction<=N; junction++) {
      String[] stones = br.readLine().split(" ");
      // List<Integer> l = new ArrayList<>();
      for (int j=1; j<stones.length; j++) {
        // add junction i to stone type
        int stone_type = Integer.parseInt(stones[j]);
        // l.add(stone_type);
        cost[junction][stone_type] = 0;
        Pair<Integer, Integer> p = new Pair<>(junction, stone_type);
        Pair<Long, Pair<Integer, Integer>> pair = new Pair<>((long) 0, p);
        pq.add(pair);
      }
      // junction_to_stones.put(junction, l);
    }
    

    // last R lines describe recipes
    for (int r=1; r<=R; r++) {
      int result_stone;
      s = br.readLine().split(" ");
      result_stone = Integer.parseInt(s[s.length-1]);
      rid_to_results[r] = result_stone;
      // stone_to_rid.get(result_stone).add(r); // add this recipe id to stone_to_rid
      rid_to_recipes.put(r, new HashMap<Integer, Integer>());
      for (int i=1; i<s.length-1; i++) {
        int ingredient = Integer.parseInt(s[i]);
        stone_to_rid.get(ingredient).add(r);
        Map<Integer, Integer> map = rid_to_recipes.get(r);
        if (map.containsKey(ingredient)) {
          map.put(ingredient, map.get(ingredient) + 1);
        } else {
          map.put(ingredient, 1);
        }
      }
    }

    // priority queue, djikstra algorithm
    while (!pq.isEmpty()) {
      // pop the first element <energy, <junction, stone_type>>, the minimum cost to get stone_type to junction
      long energy_use = pq.peek().first;
      Pair<Integer, Integer> pair = pq.peek().second;
      int junction = pair.first;
      int stone_type = pair.second;
      pq.poll();

      if (cost[junction][stone_type] != energy_use) {
        // this means the energy cost in the pq is staled, we ignore
        continue;
      }
      // we iterate this junction's neighbors
      for (int other : graph.get(junction)) {
        if (Math.min(limit, energy_use + 1) < cost[other][stone_type]) {
          // we found a smaller cost to carry stone_type to junction 'other'
          cost[other][stone_type] = Math.min(limit, energy_use + 1);

          // we push this updated candidate to our queue
          Pair<Integer, Integer> p = new Pair<>(other, stone_type);
          Pair<Long, Pair<Integer, Integer>> new_pair = new Pair<>(cost[other][stone_type], p);
          pq.add(new_pair);
        }
      }

      // then we check whether we can improve the cost by using recipes
      for (int rid : stone_to_rid.get(stone_type)) {
        long energy_req = 0;
        int result_stone = rid_to_results[rid];
        for (int stone : rid_to_recipes.get(rid).keySet()) {
          int count = rid_to_recipes.get(rid).get(stone);
          energy_req += (count * cost[junction][stone]);
        }
        energy_req = Math.min(energy_req, limit);
        if (cost[junction][result_stone] > energy_req) {
          // find a better source to get stone_type
          cost[junction][result_stone] = energy_req;
          pq.add(new Pair<Long, Pair<Integer, Integer>> (energy_req, new Pair<Integer, Integer>(junction, result_stone)));
        }
      }
    }

    long min_energy = limit;
    for (int i = 1; i<= N; i++) {
      min_energy = Math.min(min_energy, cost[i][1]);
    }

    if (min_energy >= limit) {
      min_energy = -1;
    }

    System.out.format("Case #%d: %d%n", trial, min_energy);

    return;
  }



  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int test = Integer.parseInt(br.readLine());
    for (int trial = 1; trial <= test; trial++) {
      solve(trial, br);
    }
    br.close();
  }
}

class Pair<T1, T2> {
  public T1 first;
  public T2 second;
  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }
}
