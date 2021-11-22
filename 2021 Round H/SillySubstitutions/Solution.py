import collections
import traceback
from math import *
from collections import defaultdict


def solve(): 
  """
  O(10N) solution.

  Use linked list structure: for every index, initially set each index's last node and next node to be its neighbors. Then we we merge
  two nodes, destroy the next node, i.e. a[9] = 1 and a[10] = 2, destroy node 10 and set a[9] = 3, and we check value of a[8] and a[11] 
  to determine if new neighbors will become potential pairs or old active pair should be removed. We use an active set 

  {
    0: {i1, i2, ...}
    1: {j1, j2, ...}
    ...
  }
  to keep track of starting indexs for each given digit.

  We loop through the digits from 0 to 9 until no changes made to the whole list
  """
  n = int(input())
  val = list(map(int, input()))
  prev = [i-1 for i in range(n)]
  nxt = [i+1 for i in range(n)]

  active = defaultdict(set) # active positions, where the key is the value of the node and its next node contains the value plus 1
  destroyed = set() # destroyed indices
  # initialize
  for i in range(n-1):
    if val[i+1] == (val[i]+1)%10:
      active[val[i]].add(i)

  # while loop
  change = True
  while change:
    change = False
    for d in range(10):
      if active[d]:
        change = True
        while active[d]:
          idx = active[d].pop()
          # check if idx is destroyed
          if idx in destroyed:
            continue
          # otherwise merge the neighbors
          val[idx] = (d+2)%10
          idx_next = nxt[idx]
          idx_prev = prev[idx]
          val_next = val[nxt[idx_next]] if nxt[idx_next] != n else None
          val_prev = val[idx_prev] if idx_prev != -1 else None

          # update next and prev pointers
          nxt[idx] = nxt[idx_next]
          if nxt[idx_next] != n:
            prev[nxt[idx_next]] = idx

          destroyed.add(idx_next)
          # if prev is in the active set, remove it
          if idx_prev in active[val_prev]:
            active[val_prev].remove(idx_prev)

          # check if we can add new pairs to active
          if val_next is not None and val_next == (val[idx]+1)%10:
            active[val[idx]].add(idx)
          if val_prev is not None and val[idx] == (val_prev+1)%10:
            active[val_prev].add(idx_prev)

  # reconstruct
  res = []
  i = 0
  while i < n:
    res.append(str(val[i]))
    i = nxt[i]
        
  return ''.join(res)

      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())

