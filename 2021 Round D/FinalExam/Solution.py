# for SortedList, refer to: https://github.com/grantjenks/python-sortedcontainers/blob/master/sortedcontainers/sortedlist.py

import traceback
from sortedcontainers import SortedList


def solve(): 
  """
  use a sorted list to store left and right ends of intervals.
  For each student Sj, use binary search to find first interval [l, r] (index i) such that l >= Sj.
  Get (if exists) previous interval [pl, pr], if Sj is in [pl, pr] then select Sj as the difficulty
  and split [pl, pr] at Sj to form [pl, Sj-1], [Sj+1, pr] if any is valid. 
  If Sj > pr, then if Sj - pr <= l - Sj, set res[j] = pr and remove pr.
  """
  intervals = SortedList()
  N, M = list(map(int, input().split(" ")))
  for i in range(N):
    intervals.add(tuple(list(map(int, input().split(" ")))))
    
  students = list(map(int, input().split(" ")))

  res = [0 for _ in range(M)]
  # loop
  for i in range(M):
    s = students[i]
    idx = intervals.bisect_left((s, -1))
    if idx-1 >= 0:
      pl, pr = intervals[idx-1]
      # check if pl <= s <= pr
      if s <= pr:
        intervals.pop(idx-1)
        res[i] = str(s)
        if pl < s:
          intervals.add((pl, s-1))
        if s < pr:
          intervals.add((s+1, pr))
      else:
        # s > pr
        if idx == len(intervals) or s - pr <= intervals[idx][0] - s:
          intervals.pop(idx-1)
          res[i] = str(pr)
          if pl < pr:
            intervals.add((pl, pr-1))
        else:
          # use intervals[idx]'s difficulty
          l,r = intervals.pop(idx)
          res[i] = str(l)
          if l < r:
            intervals.add((l+1, r))
    else:
      # if idx == 0, we can only use l
      l,r = intervals.pop(idx)
      res[i] = str(l)
      if l < r:
        intervals.add((l+1, r))          
  
  return ' '.join(res)


if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())

