import traceback
from math import log
from heapq import heappush, heappop
from collections import defaultdict

# ref: https://github.com/kamyu104/GoogleKickStart-2021/blob/main/Round%20F/festival4.py

def heap_remove_helper(h, to_remove, sign):
  """
  clean up the inactive values in the heap

  if sign is 1 then the heap is a min heap otherwise it is a max heap
  """
  while h and sign*h[0] in to_remove:
    v = sign*h[0]
    to_remove[v] -= 1
    if not to_remove[v]:
      del to_remove[v]
    heappop(h)


def solve(): 
  """
  Use two heaps, one min heap with size k and one max heap with infinite size to keep the

  Sort the values based on the entry or exit time of attractions 
  """

  D,N,K = list(map(int, input().split()))

  # read atractions, split by enter and exit point
  positions = []
  for _ in range(N):
    v, s, e = list(map(int, input().split()))
    # attraction exits at day e+1, and will be handled before entry of attraction starting at e+1
    positions.append((s, 1, v))
    positions.append((e+1, -1, v))
  positions.sort()
  # build two heaps, one is the main heap for biggest K attractions and the rest is for the remaining active attractions
  hmain, hrest = [], []
  to_remove = defaultdict(int)

  res, curr = 0, 0
  active_in_main = 0 # count of active attractions in main heap

  # main loop
  for day, flag, value in positions:
    if flag == 1: # entry
      heappush(hmain, value)
      curr += value
      active_in_main += 1
      if active_in_main == K+1:
        # number of counted active attractions exceeds K, we need to remove the top one from the heap and place it in the rest heap
        topv = heappop(hmain)
        curr -= topv
        active_in_main -= 1
        heappush(hrest, -topv)
      res = max(res, curr)
    else: # exit
      to_remove[value] += 1
      # check if we need to remove from the main heap
      if not hrest or -hrest[0] < value:
        curr -= value
        active_in_main -= 1
        # if we pop from the main heap, we need to supplement the main heap from the rest heap (if rest heap is not empty)
        if hrest:
          topv = -heappop(hrest)
          heappush(hmain, topv)
          curr += topv
          active_in_main += 1
    # clean up both heaps
    heap_remove_helper(hrest, to_remove, -1)
    heap_remove_helper(hmain, to_remove, 1)
  return res

      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())

