
import traceback
from math import log

def solve(): 
  """
  Use linearity of expectation. 

  Taking ith card require the ith card is the max among the first i cards, 
  where the probability is 1/i.

  Therefore, the final result will be harmonic some of first N terms 1+1/2+1/3+...+1/N 

  Since we only need accuracy approximately 10^-6, we need to approximate the accuracy when N is large.
  We can use Euler constant for harmonic series approximation.
  """

  N = int(input())

  if N <= 10**6:
    return sum([1/i for i in range(1, N+1)])

  else:
    gamma = 0.57721566490153286060651209008240243104215933593992
    return log(N) + gamma + 0.5/N - 1/(12*N**2)
      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())

