
import traceback
from collections import Counter

def solve(): 
  """
  A string can have shuffled anagram if and only if each letter occurs at most n//2 times
  """

  s = input()
  n = len(s)
  cnt = Counter()
  for i in range(n):
    cnt[s[i]] += 1

  # check counter
  for k,v in cnt.items():
    if v > n//2:
      return "IMPOSSIBLE"

  res = ["" for _ in range(n)]

  # sort s based on letter
  sorted_s = sorted(enumerate(s), key=lambda x: x[1])
  
  tmp = sorted_s[n//2:] + sorted_s[:n//2]

  # reconstruct
  for i in range(n):
    res[sorted_s[i][0]] = tmp[i][1]

  return ''.join(res)
      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())
