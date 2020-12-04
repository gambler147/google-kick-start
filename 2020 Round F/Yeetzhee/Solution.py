def solve(trial):
    def dfs(arr):
      ta = tuple(arr)
      if ta in memo: return memo[ta]
      
      pos = 0
      res = 0
      i = 0
      while i < M:
          j = i
          while j+1 < M and arr[j+1] == arr[i]:
              j += 1
          if arr[j] + 1 <= target[j]:
              arr[j] += 1
              pos += j-i+1
              res+= dfs(arr) * (j-i+1)
              arr[j]-=1
          i = j+1
      res = res / pos + M/pos
      memo[ta] = res
      return res
    s = input().split(" ")
    N, M, K = int(s[0]), int(s[1]), int(s[2])
    
    target = []
    for _ in range(M-K):
        target.append(0)
    for _ in range(K):
        target.append(int(input()))
        
    memo = {}
    memo[tuple(target)] = 0.0
    res = dfs([0] * M)
    print("Case #%d: %f"%(trial, res))
    


t = int(input())
for i in range(t):
    solve(i+1)
    