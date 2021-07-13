import traceback
from collections import defaultdict

def solve(): 
  """
  sort by L, R.
  """
  N, C = list(map(int, input().split(" ")))
  # read L, R
  entry = []
  for i in range(N):
    L,R = list(map(int, input().split(" ")))
    entry.append((L, 1))
    entry.append((R, -1))
  # sort entries
  entry.sort()
    
  # count points that have split same number of intervals
  cnt = defaultdict(int)

  # loop from left to right
  cur = 0 # current intervals
  last = -1
  i = 0
  while i < len(entry):
    p, chg = entry[i]
    ending = int(chg == -1)
    while i+1 < len(entry) and entry[i+1][0] == p:
      chg += entry[i+1][1]
      ending += int(entry[i+1][1] == -1)
      i+=1
    # count distance to previous entry
    if i > 0 and last != -1 and p - last > 1:
      cnt[cur] += p-last-1
    # count number of intervals splitting at current position
    cnt[cur-ending] += 1

    # update cur 
    cur += chg

    # update i
    i += 1
    last = p

  # sort cnt
  cnt = sorted(cnt.items(), key=lambda x: -x[0])
  # count number of intervals can be created. Initially there are N intervals
  res = N
  j = 0
  while C > 0 and j < len(cnt):
    cut = min(C, cnt[j][1])
    res += cnt[j][0] * cut
    C -= cut
    j+=1

  return res

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())
