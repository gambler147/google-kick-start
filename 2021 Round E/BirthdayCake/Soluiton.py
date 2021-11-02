
import traceback
from collections import Counter

def solve(): 
  """
  Cutting a cake with R rows and C columns
  """

  R,C,K = list(map(int, input().split()))
  r1,c1, r2, c2 = list(map(int, input().split()))

  n, m = r2-r1+1, c2-c1+1

  res = 0

  # first find minimum number of cuts to cut the rectangle from original cake
  cuts = 0

  if r1==1 or r2 == R or c1 == 1 or c2 == C:
    cuts += int(r1!=1) * ((m-1)//K + 1)
    cuts += int(r2!=R) * ((m-1)//K + 1)
    cuts += int(c1!=1) * ((n-1)//K + 1)
    cuts += int(c2!=C) * ((n-1)//K + 1)

  else:
    # rectangle is interior, find minimum cuts
    # four ways of cuts
    cuts = min(
      1+(r2-1)//K + (1+(n-1)//K) + 2*(1+(m-1)//K),
      1+(R-r1)//K + (1+(n-1)//K) + 2*(1+(m-1)//K),
      2*(1+(n-1)//K) + 1+(c2-1)//K + (1+(m-1)//K),
      2*(1+(n-1)//K) + 1+(C-c1)//K + (1+(m-1)//K),
    )

  # number of internal cuts
  cuts += n*m-1 + (n-1)//K * ((m-1)//K)
  return cuts
      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())
