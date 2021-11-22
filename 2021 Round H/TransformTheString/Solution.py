import traceback
from math import log

def solve(): 

  """
  Given String S, F, we can loop through every element f in F and find minimum the distance |c - f| for each c in S

  O(S*F)
  """

  # input
  S = input()
  F = input()

  res = 0
  for c in S:
    min_dist = 26
    for f in F:
      min_dist =  min(min_dist, min(abs(ord(c)-ord(f)), 26+ord(c)-ord(f), 26+ord(f)-ord(c)))

    res += min_dist
  return res

      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())

