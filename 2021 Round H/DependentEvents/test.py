import traceback
from math import log2, ceil
from collections import defaultdict

MOD = 10**9+7
SCALE = 10**6

def mod_inv(x, mod=MOD):
  return pow(x, mod-2, mod)

def mod_mul(x, y, mod=MOD):
  return (x*y)%MOD

class Helper:
  """
  The segment tree is to find range minimum for given nodes u,v, where u,v are the events. 
  We use dfs to build the travel list where the minium height of the nodes between first occurrences of u,v in the
  travel list will be the lca of u and v.

  It also keep track of the binary lifting parents of each node (O(NlogN)) for finding the probability P(u|parent)
  """
  def __init__(self):
    pass

  def build_from_graph(self, n, graph, probs, root=0):
    """
    graph is a dict
    probs is a list of list, where probs[i] = [p1, p2] and are probability of event i happening given its parent event happens or not
    """
    self.probs = probs
    self.height = [0] * n # height of nodes in the tree, root has a height of 0
    self.first = [0] * n # first occurrences of each node in the travel
    self.travel = []
    self.visited = [False] * n

    # probability table
    p = int(ceil(log2(n)))
    self.parent = [[-1] * p for _ in range(n)] # parent[node][i] is node's 2^i th parent
    self.uncond_prob = [0]*n  # probability of event i happens
    self.cond_prob_h0 = [[0] * p for _ in range(n)] # conditional probability given parent happens
    self.cond_prob_h1 = [[0] * p for _ in range(n)] # conditional probability given parent not happen

    # dfs to construct graph and probablity table
    self.dfs(graph, root)

    m = len(self.travel)
    self.segtree = [0] * (4*m)
    self.build(1, 0, m-1) # default root is 1
    
  def dfs(self, graph, node, h=0, parent=-1):
    self.visited[node] = True
    self.height[node] = h
    self.first[node] = len(self.travel)
    self.travel.append(node)

    # fill the probability table
    # unconditional probability
    if parent == -1:
      self.uncond_prob[node] = self.probs[node][0]
    else:
      self.uncond_prob[node] = self.uncond_prob[parent]*self.probs[node][0] + (1-self.uncond_prob[parent]) * self.probs[node][1]

    # conditional probability table
    self.parent[node][0] = parent
    self.cond_prob_h0[node][0] = self.probs[node][0]
    self.cond_prob_h1[node][0] = self.probs[node][1]
    i = 1
    while 2**i <= h:
      par = self.parent[node][i-1]
      self.parent[node][i] = self.parent[par][i-1]
      self.cond_prob_h0[node][i] = self.cond_prob_h0[node][i-1]*self.cond_prob_h0[par][i-1] + self.cond_prob_h1[node][i-1]*(1-self.cond_prob_h0[par][i-1])
      self.cond_prob_h1[node][i] = self.cond_prob_h0[node][i-1]*self.cond_prob_h1[par][i-1] + self.cond_prob_h1[node][i-1]*(1-self.cond_prob_h1[par][i-1])
      i += 1
    
    for child in graph[node]:
      if not self.visited[child]:
        self.dfs(graph, child, h+1, node)
        # when it come back to a node, we also need to add it to our travel list
        self.travel.append(node)

  def build(self, node, b, e):
    """
    b, e are indices in the travel. node is the position in segtree
    """
    if b == e:
      self.segtree[node] = self.travel[b]
    else:
      mid = (b+e) >> 1
      self.build(2*node, b, mid)
      self.build(2*node + 1, mid+1, e)
      l = self.segtree[2*node]
      r = self.segtree[2*node + 1]
      self.segtree[node] = l if self.height[l] < self.height[r] else r

  def query(self, node, b, e, L, R):
    if b > R or e < L:
      return -1
    if b >= L and e <= R:
      return self.segtree[node]

    mid = (b+e) >> 1
    left = self.query(2*node, b, mid, L, R)
    right = self.query(2*node + 1, mid+1, e, L, R)
    if left == -1: return right
    if right == -1: return left
    return left if self.height[left] < self.height[right] else right


  def lca(self, u, v):
    left = self.first[u]
    right = self.first[v]
    left, right = min(left, right), max(left,right)
    return self.query(1, 0, len(self.travel)-1, left, right)


  def query_main(self, u, v):
    # return the probablity that u, v both happen
    # get ancestor
    par = self.lca(u, v)
    # find conditional probability P(u|p) and P(v|p)
    # the final result will be P(u|p)P(v|p)*P(p) + P(u|-p)*P(v|-p)*(1-P(p))

    print(u, par)
    up0 = self.get_cond_prob(u, par, self.cond_prob_h0) # prob of u happening given par happens
    vp0 = self.get_cond_prob(v, par, self.cond_prob_h0) # prob of v happening given par happens
    up1 = self.get_cond_prob(u, par, self.cond_prob_h1, neg=True) # prob of u happening given par not happen
    vp1 = self.get_cond_prob(v, par, self.cond_prob_h1, neg=True) # prob of v happening given par not happen
    p = self.uncond_prob[par]

    res = up0*vp0*p + up1*vp1*(1-p)
    return res


  def get_cond_prob(self, node, par, cond_prob, neg=False):
    """
    conditional probability of P(u | par) or P(u | -par)
    """

    # get height of ht and u
    ht_node = self.height[node]
    ht_par = self.height[par]

    return self.get_cond_prob_for_k_ancestors(node, ht_node-ht_par, cond_prob, neg=neg)

  def get_cond_prob_for_k_ancestors(self, node, k, cond_prob, neg=False):
    """probability that node event happens given kth ancestor event happens (or not)"""
    if k == 0:
      return 1 if not neg else 0

    if (k-1)&k == 0:
      return cond_prob[node][int(log2(k))]

    # otherwise, we do dfs
    i = int(log2(k))

    print(node, i)
    mid = self.parent[node][i]
    # get probability from node
    mid_cp = self.get_cond_prob_for_k_ancestors(self, mid, k-2**i, cond_prob)
    
    # merge
    res = mid_cp * self.cond_prob_h0[node][i] + (1-mid_cp) * self.cond_prob_h1[node][i]

    return res

    


def solve(): 
  """
  the events form a tree. For each pair u,v, find LCA of the pair.

  1. construct an segment tree to find lowest common ancestor of any given pair u,v
  2. construct the probability matrix P(u | par(u, i)), where par(u, i) is the 2^i th parent of u. This can be done in dfs
  
  """
  N, Q = list(map(int, input().split()))
  # print("test")

  graph = defaultdict(list)
  probs = []
  first = int(input())
  probs.append((first/SCALE, first/SCALE))
  for i in range(1, N):
    parent, p1, p2 = list(map(int, input().split()))
    parent -= 1 # we number from 0 to N-1
    graph[parent].append(i)
    probs.append((p1/SCALE, p2/SCALE))

  helper = Helper()
  helper.build_from_graph(N, graph, probs)

  # print(helper.uncond_prob, helper.cond_prob_h0, helper.cond_prob_h1, sep='\n')
  res = []

  for q in range(Q):
    # input()
    l, r = list(map(int, input().split()))
    res.append(helper.query_main(l-1, r-1))

  # convert to string
  res = list(map(str, res))
  return ' '.join(res)

      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())

